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
    //я пробовал здесь метод, который вы мне указали в исправлениях - countByUri, но данный метод считает количество ссылок
    //тогда я изменил название и пробовал CountByIp, но данный метод должен принимать ip, потому что
    //стандартные названия методов Jpa репозиториев поддерживают лишь запрос по типу
    // select count(ip) where ip = ?1 (по крайней мере так на StackOverflow указано, что эти параметры должны совпадать)
    //также пробовал countByIpAndUriIs, но тогда метод просит два аргумента
    //поэтому оставил Query, не нашел другого решения

    @Query("select count(distinct h.ip) from hits h where h.uri = ?1")
    Long countDistinctIp(String uri);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
