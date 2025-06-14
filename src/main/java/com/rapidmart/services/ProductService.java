package com.rapidmart.services;

import com.rapidmart.dtos.ProductResponseDTO;
import com.rapidmart.models.Product;
import com.rapidmart.models.Store;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.ProductRepository;
import com.rapidmart.repositories.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ZoneRepository zoneRepository;
    private final ProductRepository productRepository;

    public List<ProductResponseDTO> getProductsByZone(Long zoneId){
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        List<ProductResponseDTO> responseList = new ArrayList<>();

        for (Store store : zone.getStores()){
            List<Product> products = productRepository.findByStore(store);

            for (Product product : products){
                ProductResponseDTO dto = new ProductResponseDTO();
                dto.setId(product.getId());
                dto.setName(product.getName());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setQuantityInStock(product.getQuantityInStock());
                responseList.add(dto);
            }
        }

        System.out.println("Zone: " + zoneId);
        System.out.println("Products: " + responseList.toString());

        return responseList;
    }
}
