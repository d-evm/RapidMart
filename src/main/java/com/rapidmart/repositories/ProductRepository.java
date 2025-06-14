package com.rapidmart.repositories;

import com.rapidmart.models.Product;
import com.rapidmart.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);
}
