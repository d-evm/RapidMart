package com.rapidmart.services;

import com.rapidmart.dtos.ZoneRequestDTO;
import com.rapidmart.dtos.ZoneResponseDTO;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public ZoneResponseDTO createZone(ZoneRequestDTO dto) {
        if (zoneRepository.existsByPincode(dto.getPincode())) {
            throw new RuntimeException("Zone with pincode already exists");
        }

        Zone zone = Zone.builder()
                .name(dto.getName())
                .pincode(dto.getPincode())
                .build();

        zone = zoneRepository.save(zone);

        return new ZoneResponseDTO(zone.getId(), zone.getName(), zone.getPincode());
    }

    public List<ZoneResponseDTO> getAllZones() {
        return zoneRepository.findAll()
                .stream()
                .map(zone -> new ZoneResponseDTO(zone.getId(), zone.getName(), zone.getPincode()))
                .collect(Collectors.toList());
    }

    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + id));
    }
}
