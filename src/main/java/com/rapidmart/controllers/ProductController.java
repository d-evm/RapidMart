package com.rapidmart.controllers;

import com.rapidmart.dtos.ProductResponseDTO;
import com.rapidmart.models.User;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.UserRepository;
import com.rapidmart.repositories.ZoneRepository;
import com.rapidmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private UserRepository userRepository;
    @Autowired private ZoneRepository zoneRepository;

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(productService.getProductsByZone(zoneId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getProductsForUserZone() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String pincode = user.getPincode();
        Zone zone = zoneRepository.findByPincode(pincode);

        return ResponseEntity.ok(productService.getProductsByZone(zone.getId()));
    }


}
