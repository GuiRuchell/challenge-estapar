package com.estapar.challenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageSector {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sector;

    private Double basePrice;
    private Integer maxCapacity;
    private LocalTime openHour;
    private LocalTime closeHour;
    private Integer durationLimitMinutes;
}
