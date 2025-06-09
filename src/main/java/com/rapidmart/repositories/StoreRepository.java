package com.rapidmart.repositories;

import com.rapidmart.models.Store;
import com.rapidmart.models.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByZone(Zone zone);
}
