package com.estapar.challenge.service;
import com.estapar.challenge.dto.RevenueRequest;
import com.estapar.challenge.dto.RevenueResponse;
import com.estapar.challenge.entity.VehicleEvent;
import com.estapar.challenge.repository.VehicleEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final VehicleEventRepository eventRepo;

    public RevenueResponse getRevenue(RevenueRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());

        List<VehicleEvent> events = eventRepo.findBySectorAndExitTimeBetween(
                request.getSector(),
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX)
        );

        double total = events.stream()
                .filter(e -> e.getFinalPrice() != null)
                .mapToDouble(VehicleEvent::getFinalPrice)
                .sum();

        return RevenueResponse.builder()
                .amount(Math.round(total * 100.0) / 100.0)
                .currency("BRL")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
