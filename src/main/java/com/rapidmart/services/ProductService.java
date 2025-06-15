package com.rapidmart.services;

import com.rapidmart.dtos.ProductRequestDTO;
import com.rapidmart.dtos.ProductResponseDTO;
import com.rapidmart.models.Product;
import com.rapidmart.models.Store;
import com.rapidmart.models.Zone;
import com.rapidmart.repositories.ProductRepository;
import com.rapidmart.repositories.StoreRepository;
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
    private final StoreRepository storeRepository;

    public List<ProductResponseDTO> getProductsByZone(Long zoneId){
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        List<ProductResponseDTO> responseList = new ArrayList<>();

        for (Store store : zone.getStores()){
            List<Product> products = productRepository.findByStore(store);

            for (Product product : products){
                ProductResponseDTO dto = ProductResponseDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantityInStock(product.getQuantityInStock())
                        .build();
                responseList.add(dto);
            }
        }

        System.out.println("Zone: " + zoneId);
        System.out.println("Products: " + responseList.toString());

        return responseList;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantityInStock(dto.getQuantityInStock())
                .store(store)
                .build();

        product = productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();
    }

    public void updateQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantityInStock(quantity);
        productRepository.save(product);
    }

}
