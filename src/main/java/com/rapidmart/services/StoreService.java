package com.rapidmart.services;

import com.rapidmart.dtos.StoreRequestDTO;
import com.rapidmart.dtos.StoreResponseDTO;
import com.rapidmart.models.Store;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ZoneService zoneService;

    public StoreResponseDTO createStore(StoreRequestDTO dto) {
        Zone zone = zoneService.getZoneById(dto.getZoneId());

        Store store = Store.builder()
                .name(dto.getName())
                .zone(zone)
                .build();

        store = storeRepository.save(store);

        return new StoreResponseDTO(store.getId(), store.getName(), store.getZone().getName());
    }

    public List<StoreResponseDTO> getStoresByZone(Long zoneId) {
        Zone zone = zoneService.getZoneById(zoneId);
        List<Store> stores = storeRepository.findByZone(zone);

        return stores.stream()
                .map(store -> new StoreResponseDTO(store.getId(), store.getName(), store.getZone().getName()))
                .collect(Collectors.toList());
    }
}
