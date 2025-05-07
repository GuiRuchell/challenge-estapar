package com.estapar.challenge.service;

import com.estapar.challenge.dto.PlateStatusResponse;
import com.estapar.challenge.entity.GarageSector;
import com.estapar.challenge.entity.Spot;
import com.estapar.challenge.entity.VehicleEvent;
import com.estapar.challenge.repository.GarageSectorRepository;
import com.estapar.challenge.repository.SpotRepository;
import com.estapar.challenge.repository.VehicleEventRepository;
import com.estapar.challenge.util.PricingCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlateStatusService {

    private final SpotRepository spotRepo;
    private final VehicleEventRepository eventRepo;
    private final GarageSectorRepository sectorRepo;

    public PlateStatusResponse getStatus(String plate) {
        Spot spot = spotRepo.findByLicensePlate(plate).orElse(null);
        VehicleEvent event = eventRepo.findTopByLicensePlateOrderByEntryTimeDesc(plate).orElse(null);

        if (spot == null || event == null) {
            return PlateStatusResponse.builder()
                    .license_plate(plate)
                    .price_until_now(0.0)
                    .build();
        }

        GarageSector sector = sectorRepo.findById(spot.getSector()).orElse(null);
        if (sector == null) return null;

        long occupied = spotRepo.countBySectorAndOccupiedTrue(sector.getSector());
        double price = PricingCalculator.applyDynamicPricing(
                sector.getBasePrice(), (int) occupied, sector.getMaxCapacity()
        );

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(spot.getEntryTime(), now);
        double hours = Math.max(duration.toMinutes() / 60.0, 0.5); // mínimo 30min

        double finalPrice = Math.round(hours * price * 100.0) / 100.0;

        return PlateStatusResponse.builder()
                .license_plate(plate)
                .entry_time(spot.getEntryTime())
                .time_parked(now)
                .lat(spot.getLat())
                .lng(spot.getLng())
                .price_until_now(finalPrice)
                .build();
    }
}