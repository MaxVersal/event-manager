package ru.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class ApiError {
    private String status;

    private String reason;

    private String message;

    private LocalDateTime timestamp;
}
