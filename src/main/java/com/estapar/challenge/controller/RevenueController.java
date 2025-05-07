package com.estapar.challenge.controller;

import com.estapar.challenge.dto.RevenueRequest;
import com.estapar.challenge.dto.RevenueResponse;
import com.estapar.challenge.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService service;

    @GetMapping
    public RevenueResponse getRevenue(@RequestBody RevenueRequest request) {
        return service.getRevenue(request);
    }
}
