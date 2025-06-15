package com.rapidmart.controllers;

import com.rapidmart.dtos.ProductRequestDTO;
import com.rapidmart.dtos.ProductResponseDTO;
import com.rapidmart.models.User;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.UserRepository;
import com.rapidmart.repositories.ZoneRepository;
import com.rapidmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CO_ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/{productId}/quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'CO_ADMIN')")
    public ResponseEntity<String> updateQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        productService.updateQuantity(productId, quantity);
        return ResponseEntity.ok("Quantity updated.");
    }


}
