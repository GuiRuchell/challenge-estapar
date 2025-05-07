package com.estapar.challenge.repository;

import com.estapar.challenge.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByLatAndLng(Double lat, Double lng);
    Optional<Spot> findByLicensePlate(String licensePlate);
    long countBySectorAndOccupiedTrue(String sector);
}
