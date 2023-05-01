package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.user.model.User;

import java.util.Optional;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r where r.user.id = :userId and r.event.id = :eventId")
    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countAllByStatusEqualsAndEvent_Id(Status status, Long eventId);

    List<Request> findAllByUserIs(User user);

    List<Request> findAllByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId);

    List<Request> findAllByIdIn(List<Long> ids);
}
