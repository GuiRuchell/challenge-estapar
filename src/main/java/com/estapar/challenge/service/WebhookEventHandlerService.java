package com.estapar.challenge.service;

import com.estapar.challenge.dto.WebhookEventDto;
import com.estapar.challenge.entity.GarageSector;
import com.estapar.challenge.entity.Spot;
import com.estapar.challenge.entity.VehicleEvent;
import com.estapar.challenge.repository.GarageSectorRepository;
import com.estapar.challenge.repository.SpotRepository;
import com.estapar.challenge.repository.VehicleEventRepository;
import com.estapar.challenge.util.PricingCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookEventHandlerService {

    private final SpotRepository spotRepo;
    private final VehicleEventRepository eventRepo;
    private final GarageSectorRepository garageSectorRepository;

    @Transactional
    public void handle(WebhookEventDto dto) {
        log.info("Recebido evento: {}", dto);

        switch (dto.getEvent_type()) {
            case "ENTRY" -> handleEntry(dto);
            case "PARKED" -> handleParked(dto);
            case "EXIT" -> handleExit(dto);
        }
    }

    private void handleEntry(WebhookEventDto dto) {
        VehicleEvent entry = new VehicleEvent();
        entry.setLicensePlate(dto.getLicense_plate());
        entry.setEntryTime(dto.getEntry_time());
        entry.setEventType("ENTRY");
        eventRepo.save(entry);
    }

    private void handleParked(WebhookEventDto dto) {
        Optional<Spot> opt = spotRepo.findByLatAndLng(dto.getLat(), dto.getLng());
        if (opt.isEmpty()) return;

        Spot spot = opt.get();
        spot.setOccupied(true);
        spot.setLicensePlate(dto.getLicense_plate());
        spot.setEntryTime(dto.getEntry_time());
        spotRepo.save(spot);

        eventRepo.findTopByLicensePlateOrderByEntryTimeDesc(dto.getLicense_plate()).ifPresent(event -> {
            event.setSector(spot.getSector());
            event.setEventType("PARKED");
            eventRepo.save(event);
        });
    }

    private void handleExit(WebhookEventDto dto) {
        eventRepo.findTopByLicensePlateOrderByEntryTimeDesc(dto.getLicense_plate()).ifPresent(event -> {
            event.setExitTime(dto.getExit_time());
            event.setEventType("EXIT");

            if (event.getSector() != null) {
                GarageSector sector = garageSectorRepository.findById(event.getSector()).orElse(null);
                if (sector != null) {
                    long occupied = spotRepo.countBySectorAndOccupiedTrue(sector.getSector());
                    double dynamicPrice = PricingCalculator.applyDynamicPricing(
                            sector.getBasePrice(), (int) occupied, sector.getMaxCapacity()
                    );

                    Spot spot = spotRepo.findByLicensePlate(dto.getLicense_plate()).orElse(null);
                    if (spot != null) {
                        double hours = Math.max(Duration.between(spot.getEntryTime(), dto.getExit_time()).toMinutes() / 60.0, 0.5);
                        double finalPrice = Math.round(hours * dynamicPrice * 100.0) / 100.0;
                        event.setFinalPrice(finalPrice);
                    }
                }
            }

            eventRepo.save(event);
        });

        // Liberar vaga
        spotRepo.findByLicensePlate(dto.getLicense_plate()).ifPresent(spot -> {
            spot.setOccupied(false);
            spot.setLicensePlate(null);
            spot.setEntryTime(null);
            spotRepo.save(spot);
        });
    }
}
