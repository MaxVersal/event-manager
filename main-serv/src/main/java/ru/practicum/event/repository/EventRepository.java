package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User user, Pageable pageable);

    @Query("select e from Event e where e.id=?1 and e.initiator.id=?2")
    Event findByEventIdAndUser(Long eventId, Long userId);

    @Query("select e from Event e where e.initiator.id in :users" +
            " and e.state in :states" +
            " and e.category.id in :categories" +
            " and e.eventDate between :start and :end")
    List<Event> findForAdmin(List<Long> users,
                             List<State> states,
                             List<Long> categories,
                             LocalDateTime start,
                             LocalDateTime end,
                             Pageable pageable);

    @Query("select e from Event e where (upper(e.annotation) like upper((concat('%', :text, '%'))) " +
            "or upper(e.description) like upper(concat('%', :text, '%')) ) " +
            "and e.category.id in :categories " +
            "and e.paid = :paid " +
            "and e.eventDate between :start and :end")
    List<Event> publicSearch(String text,
                             List<Long> categories,
                             Boolean paid,
                             LocalDateTime start,
                             LocalDateTime end,
                             Pageable pageable);
}
