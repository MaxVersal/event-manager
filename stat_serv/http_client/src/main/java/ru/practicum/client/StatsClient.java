package ru.practicum.client;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.util.List;

@Slf4j
@Service
public class StatsClient {
    private final WebClient client;

    @Autowired
    public StatsClient(WebClient.Builder builder,
                       @Value("${stats.server.url}") String url) {
        this.client = builder
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)))
                .build();
    }

    public Mono<EndpointHitDto> save(EndpointHitDto endpointHit) {
        return client.post()
                .uri("/hit")
                .body(Mono.just(endpointHit), EndpointHitDto.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("POST request /hit statusCode={}", response.statusCode());
                        return response.bodyToMono(EndpointHitDto.class);
                    } else {
                        log.error("POST request /hit statusCode={}", response.statusCode());
                        return response.createException().flatMap(Mono::error);
                    }
                });
    }

    public Flux<Object> findStats(String start, String end, List<String> uris, Boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("GET request /stats?start={}&end={}&uris={}&unique={} statusCode={}",
                                start, end, uris, unique, response.statusCode());
                        return response.bodyToFlux(ViewStats.class);
                    } else {
                        log.error("GET request /stats?start={}&end={}&uris={}&unique={} statusCode={}",
                                start, end, uris, unique, response.statusCode());
                        return response.createException().flatMapMany(Flux::error);
                    }
                });
    }
}