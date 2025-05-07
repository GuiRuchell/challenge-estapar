package com.estapar.challenge.repository;

import com.estapar.challenge.entity.VehicleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VehicleEventRepository extends JpaRepository<VehicleEvent, Long> {
    Optional<VehicleEvent> findTopByLicensePlateOrderByEntryTimeDesc(String plate);
    List<VehicleEvent> findBySectorAndExitTimeBetween(String sector, LocalDate start, LocalDate end);
}
