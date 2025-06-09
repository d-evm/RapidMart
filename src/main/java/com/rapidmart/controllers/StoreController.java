package com.rapidmart.controllers;

import com.rapidmart.dtos.StoreRequestDTO;
import com.rapidmart.dtos.StoreResponseDTO;
import com.rapidmart.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@RequestBody StoreRequestDTO dto) {
        StoreResponseDTO createdStore = storeService.createStore(dto);
        return ResponseEntity.ok(createdStore);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<StoreResponseDTO>> getStoresByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(storeService.getStoresByZone(zoneId));
    }
}
