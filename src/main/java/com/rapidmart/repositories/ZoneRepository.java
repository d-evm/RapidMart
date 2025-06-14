package com.rapidmart.repositories;

import com.rapidmart.models.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByPincode(String pincode);
    Zone findByPincode(String pincode);
}
