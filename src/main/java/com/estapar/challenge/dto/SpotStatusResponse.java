package com.estapar.challenge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SpotStatusResponse {
    private boolean ocupied;
    private String license_plate;
    private double price_until_now;
    private LocalDateTime entry_time;
    private LocalDateTime time_parked;
}
