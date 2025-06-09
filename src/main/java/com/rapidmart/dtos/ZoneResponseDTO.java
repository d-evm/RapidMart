package com.rapidmart.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponseDTO {
    private Long id;
    private String name;
    private String pincode;
}
