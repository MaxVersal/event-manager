package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    List<Hit> findAllByUriInAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select count(h.ip) from hits h where h.uri =?1")
    Long countIp(String uri);

    @Query("select count(distinct h.ip) from hits h where h.uri = ?1")
    Long countDistinctIp(String uri);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
