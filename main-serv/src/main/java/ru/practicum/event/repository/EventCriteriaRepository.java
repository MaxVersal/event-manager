package ru.practicum.event.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventCriteriaRepository {

    private final EntityManager em;

    private final CriteriaBuilder cb;

    @Autowired
    public EventCriteriaRepository(EntityManager entityManager) {
        this.em = entityManager;
        this.cb = entityManager.getCriteriaBuilder();
    }

    public List<Event> getEvents(String text,
                                 List<Long> userIds,
                                 List<Long> categories,
                                 List<State> states,
                                 Boolean paid,
                                 String rangeStart,
                                 String rangeEnd,
                                 Boolean onlyAvailable,
                                 Integer from,
                                 Integer size) {
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = cb.conjunction();

        if (categories != null && !categories.isEmpty()) {
            Predicate containCategories = root.get("category").in(categories);
            criteria = cb.and(criteria, containCategories);
        }
        if ((rangeStart != null && !rangeStart.isEmpty()) && (rangeEnd != null && !rangeEnd.isEmpty())) {
            final LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            final LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Predicate greaterRangeStart = cb.between(root.get("eventDate").as(LocalDateTime.class), start, end);
            criteria = cb.and(criteria, greaterRangeStart);
        }
        if (text != null) {
            Predicate containAnnotation = cb.like(cb.lower(root.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate containDescription = cb.like(cb.lower(root.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate containText = cb.or(containAnnotation, containDescription);

            criteria = cb.and(criteria, containText);
        }
        if (paid != null) {
            Predicate isPaid;
            if (paid) {
                isPaid = cb.isTrue(root.get("paid"));
            } else {
                isPaid = cb.isFalse(root.get("paid"));
            }
            criteria = cb.and(criteria, isPaid);
        }
        if (onlyAvailable != null) {
            Predicate isAvailable;
            isAvailable = root.get("state").in(State.PUBLISHED);
            criteria = cb.and(criteria, isAvailable);
        }

        if (userIds != null && !userIds.isEmpty()) {
            Predicate containUsers = root.get("initiator").in(userIds);
            criteria = cb.and(criteria, containUsers);
        }
        if (states != null) {
            Predicate containState = root.get("state").in(states);
            criteria = cb.and(criteria, containState);
        }
        query.select(root).where(criteria);
        List<Event> events = em.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return events;
    }
}
