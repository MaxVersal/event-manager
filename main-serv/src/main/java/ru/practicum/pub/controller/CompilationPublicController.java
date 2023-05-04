package ru.practicum.pub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable @Positive Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                                @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }
}
