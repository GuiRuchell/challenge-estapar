package com.estapar.challenge.controller;

import com.estapar.challenge.dto.WebhookEventDto;
import com.estapar.challenge.service.WebhookEventHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookEventHandlerService eventHandlerService;

    @PostMapping
    public ResponseEntity<Void> receiveEvent(@RequestBody WebhookEventDto dto) {
        eventHandlerService.handle(dto);
        return ResponseEntity.ok().build();
    }
}
