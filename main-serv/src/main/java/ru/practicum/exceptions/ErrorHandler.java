package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleNumberFormatException(NumberFormatException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSqlException(SQLException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.CONFLICT.name(),
                "Integrity constraint has been violated.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.NOT_FOUND.name(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.CONFLICT.name(),
                "Required request body is missing.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.CONFLICT.name(),
                "Integrity constraint has been violated.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(),
                "Validation failed.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handlerServletException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(),
                "Required request parameter.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleTransaction(TransactionSystemException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(),
                "Check request body",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleBadRequest(ConflictException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.CONFLICT.name(),
                "For the requested operation the conditions are not met.",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleThrowable(Throwable e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(),
                "Field can't be bull",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(EventDateException.class)
    public ResponseEntity<ApiError> handleEventDate(EventDateException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(HttpStatus.CONFLICT.name(),
                "Incorrect event date",
                e.getLocalizedMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
