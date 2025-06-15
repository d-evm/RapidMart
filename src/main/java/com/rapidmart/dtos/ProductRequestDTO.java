package com.rapidmart.dtos;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private double price;
    private Long storeId;
    private int quantityInStock;
}
