package com.estapar.challenge.dto;

import lombok.Data;

@Data
public class RevenueRequest {
    private String date;   // formato: YYYY-MM-DD
    private String sector;
}