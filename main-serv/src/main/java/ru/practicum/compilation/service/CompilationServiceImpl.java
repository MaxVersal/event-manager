package ru.practicum.compilation.service;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.UpdateCompilation;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.dto.CompilationAccept;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventResponse;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventMapper eventMapper;

    private final CompilationMapper compilationMapper;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ResponseEntity<CompilationDto> postCompilation(CompilationAccept compilationAccept) {
        final Compilation compilation = compilationMapper.toEntity(compilationAccept);

        List<Event> events = eventRepository.findAllById(compilationAccept.getEvents());
        compilation.setEvents(events);
        List<EventResponse> eventResponses = events.stream().map(eventMapper::toEventResponse).collect(Collectors.toList());
        final CompilationDto dto = compilationMapper.toDto(compilationRepository.save(compilation));
        dto.setEvents(eventResponses);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }

    @Override
    public ResponseEntity<CompilationDto> getCompilationById(Long compId) {
        final Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d not found", compId))
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationMapper.toDto(compilation));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteById(Long compId) {
        final Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d not found", compId))
        );
        compilationRepository.deleteById(compilation.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Успешное удаление");
    }

    @Override
    @Transactional
    public ResponseEntity<CompilationDto> patchCompilation(UpdateCompilation updateCompilation, Long compId) {
        final Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d not found", compId))
        );
        List<Event> events = eventRepository.findAllById(updateCompilation.getEvents());
        Optional.ofNullable(updateCompilation.getTitle()).ifPresent(c -> {
            if (!StringUtils.isBlank(updateCompilation.getTitle())) compilation.setTitle(updateCompilation.getTitle());
        });
        Optional.ofNullable(updateCompilation.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateCompilation.getEvents()).ifPresent(c -> compilation.setEvents(events));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationMapper.toDto(compilationRepository.save(compilation)));
    }

    @Override
    public ResponseEntity<List<CompilationDto>> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(compilationRepository.findAllByPinnedIs(pinned, PageRequest.of(from / size, size))
                            .stream().map(compilationMapper::toDto)
                            .collect(Collectors.toList()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(compilationRepository.findAll(PageRequest.of(from / size, size))
                            .stream().map(compilationMapper::toDto)
                            .collect(Collectors.toList()));
        }

    }
}

