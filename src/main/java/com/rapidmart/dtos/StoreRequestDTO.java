package com.rapidmart.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDTO {
    private String name;
    private Long zoneId;
}
