package com.rapidmart.dtos;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private Long orderId;
    private String status;
    private int minutesRemaining;
}
