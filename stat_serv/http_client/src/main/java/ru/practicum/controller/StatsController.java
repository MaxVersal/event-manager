package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsClient client;

    @GetMapping("/stats")
    public Flux<Object> getStats(@RequestParam(value = "start") String start,
                                 @RequestParam(value = "end") String end,
                                 @RequestParam(value = "uris", required = false) List<String> uris,
                                 @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Send get request /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return client.findStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EndpointHitDto> createHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST request with body {}", endpointHitDto);
        return client.save(endpointHitDto);
    }
}
