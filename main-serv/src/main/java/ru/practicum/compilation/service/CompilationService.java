package ru.practicum.compilation.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.compilation.dto.CompilationAccept;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.UpdateCompilation;

import javax.transaction.Transactional;
import java.util.List;

public interface CompilationService {
    @Transactional
    ResponseEntity<CompilationDto> postCompilation(CompilationAccept compilationAccept);

    @Transactional
    ResponseEntity<CompilationDto> getCompilationById(Long compId);

    @Transactional
    ResponseEntity<Object> deleteById(Long compId);

    @Transactional
    ResponseEntity<CompilationDto> patchCompilation(UpdateCompilation updateCompilation, Long compId);

    @Transactional
    ResponseEntity<List<CompilationDto>> getAllCompilations(Boolean pinned, Integer from, Integer size);
}
