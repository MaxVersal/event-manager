package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequestStatusUpdateResult {
    @NotNull
    private List<ParticipationRequestDto> confirmedRequests;
    @NotNull
    private List<ParticipationRequestDto> rejectedRequests;
}
