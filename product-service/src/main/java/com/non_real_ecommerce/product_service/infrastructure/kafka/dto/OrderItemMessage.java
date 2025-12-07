package com.non_real_ecommerce.product_service.infrastructure.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Usamos @Data para obtener getters, setters, toString, etc.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemMessage {
    private Long productId;
    private Long quantity;
}
