package com.estapar.challenge.service;

import com.estapar.challenge.entity.GarageSector;
import com.estapar.challenge.entity.Spot;
import com.estapar.challenge.repository.GarageSectorRepository;
import com.estapar.challenge.repository.SpotRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GarageImportService {

    private final GarageSectorRepository sectorRepository;
    private final SpotRepository spotRepository;

    @Value("${garage.api.url}")
    private String garageApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void loadGarageData() {
        try {
            log.info("Importando dados da garagem...");

            Map<String, List<Map<String, Object>>> response = restTemplate.getForObject(garageApiUrl, Map.class);
            List<Map<String, Object>> sectors = response.get("garage");
            List<Map<String, Object>> spots = response.get("spots");

            // Importar setores
            for (Map<String, Object> s : sectors) {
                GarageSector sector = new GarageSector(
                        (String) s.get("sector"),
                        ((Number) s.get("basePrice")).doubleValue(),
                        ((Number) s.get("max_capacity")).intValue(),
                        LocalTime.parse((String) s.get("open_hour")),
                        LocalTime.parse((String) s.get("close_hour")),
                        ((Number) s.get("duration_limit_minutes")).intValue()
                );
                sectorRepository.save(sector);
            }

            // Importar vagas
            for (Map<String, Object> sp : spots) {
                Spot spot = new Spot();
                spot.setSector((String) sp.get("sector"));
                spot.setLat(((Number) sp.get("lat")).doubleValue());
                spot.setLng(((Number) sp.get("lng")).doubleValue());
                spot.setOccupied(false);
                spotRepository.save(spot);
            }

            log.info("Importação da garagem finalizada com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao importar garagem: ", e);
        }
    }
}
