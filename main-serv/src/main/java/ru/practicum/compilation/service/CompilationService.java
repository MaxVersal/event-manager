package ru.practicum.compilation.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.compilation.dto.CompilationAccept;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.UpdateCompilation;

import javax.transaction.Transactional;
import java.util.List;

public interface CompilationService {

    ResponseEntity<CompilationDto> postCompilation(CompilationAccept compilationAccept);

    ResponseEntity<CompilationDto> getCompilationById(Long compId);

    ResponseEntity<Object> deleteById(Long compId);

    ResponseEntity<CompilationDto> patchCompilation(UpdateCompilation updateCompilation, Long compId);

    ResponseEntity<List<CompilationDto>> getAllCompilations(Boolean pinned, Integer from, Integer size);
}
