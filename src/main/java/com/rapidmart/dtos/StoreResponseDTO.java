package com.rapidmart.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String zoneName;
}
