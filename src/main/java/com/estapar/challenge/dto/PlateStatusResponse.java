package com.estapar.challenge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PlateStatusResponse {
    private String license_plate;
    private double price_until_now;
    private LocalDateTime entry_time;
    private LocalDateTime time_parked;
    private Double lat;
    private Double lng;
}
