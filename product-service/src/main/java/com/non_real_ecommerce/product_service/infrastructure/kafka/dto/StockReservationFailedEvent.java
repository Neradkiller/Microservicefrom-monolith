package com.non_real_ecommerce.product_service.infrastructure.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationFailedEvent {
    private UUID orderId;
    private String reason;
}
