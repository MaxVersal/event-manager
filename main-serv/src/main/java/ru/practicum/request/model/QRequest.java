package ru.practicum.request.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QRequest extends EntityPathBase<Request> {

    private static final long serialVersionUID = -1789910156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequest request = new QRequest("request");

    public final DateTimePath<LocalDateTime> created = createDateTime("created", java.time.LocalDateTime.class);

    public final ru.practicum.event.model.QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final ru.practicum.user.model.QUser user;

    public QRequest(String variable) {
        this(Request.class, forVariable(variable), INITS);
    }

    public QRequest(Path<? extends Request> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequest(PathMetadata metadata, PathInits inits) {
        this(Request.class, metadata, inits);
    }

    public QRequest(Class<? extends Request> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new ru.practicum.event.model.QEvent(forProperty("event"), inits.get("event")) : null;
        this.user = inits.isInitialized("user") ? new ru.practicum.user.model.QUser(forProperty("user")) : null;
    }

}
