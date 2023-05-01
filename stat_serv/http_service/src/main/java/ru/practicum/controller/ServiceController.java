package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ServiceController {
    private final StatsService service;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(value = "start") String start,
                                    @RequestParam(value = "end") String end,
                                    @RequestParam(value = "uris", required = false) List<String> uris,
                                    @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("GET request /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST request with body {}", endpointHitDto);
        return service.save(endpointHitDto);
    }

}
