package ru.practicum.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.validation.IpAddress;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "hits")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app", nullable = false)
    String app;

    @Column(name = "uri", nullable = false)
    String uri;

    @Column(name = "ip", nullable = false)
    @IpAddress
    String ip;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;

}
