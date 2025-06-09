package com.rapidmart.controllers;

import com.rapidmart.dtos.ZoneRequestDTO;
import com.rapidmart.dtos.ZoneResponseDTO;
import com.rapidmart.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @PostMapping
    public ResponseEntity<ZoneResponseDTO> createZone(@RequestBody ZoneRequestDTO dto) {
        ZoneResponseDTO createdZone = zoneService.createZone(dto);
        return ResponseEntity.ok(createdZone);
    }

    @GetMapping
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }
}
