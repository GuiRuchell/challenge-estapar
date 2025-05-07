package com.estapar.challenge.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebhookEventDto {
    private String license_plate;
    private LocalDateTime entry_time;
    private LocalDateTime exit_time;
    private String event_type; // ENTRY, PARKED, EXIT
    private Double lat;
    private Double lng;
}
