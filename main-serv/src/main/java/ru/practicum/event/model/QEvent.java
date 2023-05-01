package ru.practicum.event.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = -299444598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final StringPath annotation = createString("annotation");

    public final ru.practicum.category.model.QCategory category;

    public final NumberPath<Integer> confirmedRequests = createNumber("confirmedRequests", Integer.class);

    public final DateTimePath<LocalDateTime> createdOn = createDateTime("createdOn", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> eventDate = createDateTime("eventDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ru.practicum.user.model.QUser initiator;

    public final QLocation location;

    public final BooleanPath paid = createBoolean("paid");

    public final NumberPath<Integer> participantLimit = createNumber("participantLimit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> publishedOn = createDateTime("publishedOn", java.time.LocalDateTime.class);

    public final BooleanPath requestModeration = createBoolean("requestModeration");

    public final EnumPath<State> state = createEnum("state", State.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new ru.practicum.category.model.QCategory(forProperty("category")) : null;
        this.initiator = inits.isInitialized("initiator") ? new ru.practicum.user.model.QUser(forProperty("initiator")) : null;
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
    }

}