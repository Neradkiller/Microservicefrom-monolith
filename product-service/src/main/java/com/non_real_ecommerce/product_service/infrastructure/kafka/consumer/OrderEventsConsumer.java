package com.non_real_ecommerce.product_service.infrastructure.kafka.consumer;

import com.non_real_ecommerce.product_service.domain.exception.ConcurrencyException;
import com.non_real_ecommerce.product_service.domain.exception.InsufficientStockException;
import com.non_real_ecommerce.product_service.domain.port.input.ProductService;
import com.non_real_ecommerce.product_service.infrastructure.kafka.dto.OrderCreatedEvent;
import com.non_real_ecommerce.product_service.infrastructure.kafka.dto.StockReservationFailedEvent;
import com.non_real_ecommerce.product_service.infrastructure.kafka.dto.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventsConsumer {

    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order.events", groupId = "product-service-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order ID: {}", event.getOrderId());

        try {
            // Itera sobre los ítems del evento y reserva el stock para cada uno.
            // Aquí se aplica la lógica de bloqueo optimista que implementamos.
            event.getItems().forEach(item -> 
                productService.reserveStock(item.getProductId(), item.getQuantity())
            );

            // Si todas las reservas fueron exitosas, publica un evento de éxito.
            StockReservedEvent successEvent = new StockReservedEvent(event.getOrderId());
            log.info("Stock successfully reserved for order ID: {}. Publishing StockReservedEvent.", event.getOrderId());
            kafkaTemplate.send("product.events", successEvent);

        } catch (InsufficientStockException | ConcurrencyException e) {
            // Si falla por falta de stock o por un conflicto de concurrencia,
            // publica un evento de fallo.
            log.error("Stock reservation failed for order ID: {}. Reason: {}. Publishing StockReservationFailedEvent.", 
                event.getOrderId(), e.getMessage());
            
            StockReservationFailedEvent failureEvent = new StockReservationFailedEvent(event.getOrderId(), e.getMessage());
            kafkaTemplate.send("product.events", failureEvent);
            
            // NOTA: En un sistema real, aquí también se iniciaría una transacción compensatoria
            // para revertir cualquier reserva parcial que se haya podido hacer si el bucle no es transaccional.
            // En nuestro caso, @Transactional en el service debería encargarse de que el bucle sea todo o nada.
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada.
            log.error("An unexpected error occurred while processing order ID: {}. Reason: {}", 
                event.getOrderId(), e.getMessage(), e);

            StockReservationFailedEvent failureEvent = new StockReservationFailedEvent(event.getOrderId(), "Unexpected error in product service.");
            kafkaTemplate.send("product.events", failureEvent);
        }
    }
}
