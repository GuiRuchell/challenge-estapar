package com.estapar.challenge.controller;

import com.estapar.challenge.dto.PlateStatusRequest;
import com.estapar.challenge.dto.PlateStatusResponse;
import com.estapar.challenge.service.PlateStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plate-status")
@RequiredArgsConstructor
public class PlateStatusController {

    private final PlateStatusService service;

    @PostMapping
    public PlateStatusResponse getPlateStatus(@RequestBody PlateStatusRequest request) {
        return service.getStatus(request.getLicense_plate());
    }
}
