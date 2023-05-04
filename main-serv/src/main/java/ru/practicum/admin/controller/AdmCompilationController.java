package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationAccept;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.UpdateCompilation;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdmCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> postCompilation(@RequestBody @Valid CompilationAccept compilationAccept) {
        return compilationService.postCompilation(compilationAccept);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteById(@PathVariable @Positive Long compId) {
        return compilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchById(@RequestBody UpdateCompilation updateCompilation,
                                                    @PathVariable @Positive Long compId) {
        return compilationService.patchCompilation(updateCompilation, compId);
    }
}
