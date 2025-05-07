package com.estapar.challenge.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RevenueResponse {
    private double amount;
    private String currency;
    private LocalDateTime timestamp;
}
