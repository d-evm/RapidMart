package com.rapidmart.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String pincode;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Store> stores;
}
