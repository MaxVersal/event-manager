package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    @NotBlank(message = "поле app не может быть пустым")
    @NotNull(message = "необходимо добавить поле app")
    String app;

    @NotBlank(message = "поле uri не может быть пустым")
    @NotNull(message = "необходимо добавить поле uri")
    String uri;


    @NotBlank(message = "поле ip не может быть пустым")
    @NotNull(message = "необходимо добавить поле ip")
    String ip;

    @NotNull(message = "необходимо добавить поле timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
