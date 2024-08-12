package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendStatus is a Querydsl query type for FriendStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendStatus extends EntityPathBase<FriendStatus> {

    private static final long serialVersionUID = -1426082262L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendStatus friendStatus = new QFriendStatus("friendStatus");

    public final BooleanPath accepted = createBoolean("accepted");

    public final QMember accepter;

    public final BooleanPath denied = createBoolean("denied");

    public final NumberPath<Long> friendStatusId = createNumber("friendStatusId", Long.class);

    public final QMember requester;

    public QFriendStatus(String variable) {
        this(FriendStatus.class, forVariable(variable), INITS);
    }

    public QFriendStatus(Path<? extends FriendStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendStatus(PathMetadata metadata, PathInits inits) {
        this(FriendStatus.class, metadata, inits);
    }

    public QFriendStatus(Class<? extends FriendStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accepter = inits.isInitialized("accepter") ? new QMember(forProperty("accepter"), inits.get("accepter")) : null;
        this.requester = inits.isInitialized("requester") ? new QMember(forProperty("requester"), inits.get("requester")) : null;
    }

}

