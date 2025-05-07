package com.estapar.challenge.controller;

import com.estapar.challenge.dto.SpotStatusRequest;
import com.estapar.challenge.dto.SpotStatusResponse;
import com.estapar.challenge.service.SpotStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spot-status")
@RequiredArgsConstructor
public class SpotStatusController {

    private final SpotStatusService service;

    @PostMapping
    public SpotStatusResponse getSpotStatus(@RequestBody SpotStatusRequest request) {
        return service.getStatus(request.getLat(), request.getLng());
    }
}
