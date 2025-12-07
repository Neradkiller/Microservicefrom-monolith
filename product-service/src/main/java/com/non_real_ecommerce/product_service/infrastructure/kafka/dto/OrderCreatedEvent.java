package com.non_real_ecommerce.product_service.infrastructure.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private UUID orderId;
    private String userId; // o el tipo de dato que uses para el ID de usuario
    private List<OrderItemMessage> items;
}
