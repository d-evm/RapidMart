package com.rapidmart.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private String storeName;
    private String deliveryPincode;
    private LocalDateTime orderTime;
    private String estimatedDeliveryTime;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private String productName;
        private int quantity;
        private double price;
    }
}
