package com.estapar.challenge.service;

import com.estapar.challenge.dto.SpotStatusResponse;
import com.estapar.challenge.entity.GarageSector;
import com.estapar.challenge.entity.Spot;
import com.estapar.challenge.repository.GarageSectorRepository;
import com.estapar.challenge.repository.SpotRepository;
import com.estapar.challenge.util.PricingCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SpotStatusService {

    private final SpotRepository spotRepo;
    private final GarageSectorRepository sectorRepo;

    public SpotStatusResponse getStatus(Double lat, Double lng) {
        Spot spot = spotRepo.findByLatAndLng(lat, lng).orElse(null);
        if (spot == null) return null;

        if (!spot.isOccupied()) {
            return SpotStatusResponse.builder()
                    .ocupied(false)
                    .license_plate("")
                    .price_until_now(0.0)
                    .entry_time(null)
                    .time_parked(null)
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
        double hours = Math.max(duration.toMinutes() / 60.0, 0.5);

        double finalPrice = Math.round(hours * price * 100.0) / 100.0;

        return SpotStatusResponse.builder()
                .ocupied(true)
                .license_plate(spot.getLicensePlate())
                .entry_time(spot.getEntryTime())
                .time_parked(now)
                .price_until_now(finalPrice)
                .build();
    }
}
