package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatRepository repository;

    @Autowired
    private final HitMapper mapper;

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (uris != null) {
            if (unique) {
                List<ViewStats> viewStatsList = repository.findAllByUriInAndTimestampBetween(uris,
                                LocalDateTime.parse(start, pattern),
                                LocalDateTime.parse(end, pattern))
                        .stream()
                        .map(mapper::toStat)
                        .peek((viewStats) -> viewStats.setHits(repository.countDistinctIp(viewStats.getUri())))
                        .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                        .filter(distinctByKey((ViewStats::getUri)))
                        .collect(Collectors.toList());
                return viewStatsList;
            } else {
                List<ViewStats> viewStatsList = repository.findAllByUriInAndTimestampBetween(uris,
                                LocalDateTime.parse(start, pattern),
                                LocalDateTime.parse(end, pattern))
                        .stream()
                        .map(mapper::toStat)
                        .peek((viewStats) -> viewStats.setHits(repository.countIp(viewStats.getUri())))
                        .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                        .filter(distinctByKey((ViewStats::getUri)))
                        .collect(Collectors.toList());
                return viewStatsList;
            }
        }
        List<Hit> hits = repository.findAllByTimestampBetween(LocalDateTime.parse(start, pattern), LocalDateTime.parse(end, pattern));
        if (unique) {
            return hits
                    .stream()
                    .map(mapper::toStat)
                    .peek((viewStats) -> viewStats.setHits(repository.countDistinctIp(viewStats.getUri())))
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .filter(distinctByKey((ViewStats::getUri)))
                    .collect(Collectors.toList());
        } else {
            return hits
                    .stream()
                    .map(mapper::toStat)
                    .peek((viewStats) -> viewStats.setHits(repository.countIp(viewStats.getUri())))
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .filter(distinctByKey((ViewStats::getUri)))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        log.info("saving to db {}", endpointHitDto);
        return mapper.toDto(repository.save(mapper.toEntity(endpointHitDto)));
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
