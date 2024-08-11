package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupStatus is a Querydsl query type for GroupStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupStatus extends EntityPathBase<GroupStatus> {

    private static final long serialVersionUID = 1829507607L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupStatus groupStatus = new QGroupStatus("groupStatus");

    public final BooleanPath accepted = createBoolean("accepted");

    public final BooleanPath banned = createBoolean("banned");

    public final BooleanPath exited = createBoolean("exited");

    public final QGroup group;

    public final QMember groupLeader;

    public final QMember groupMember;

    public final NumberPath<Long> groupStatusId = createNumber("groupStatusId", Long.class);

    public QGroupStatus(String variable) {
        this(GroupStatus.class, forVariable(variable), INITS);
    }

    public QGroupStatus(Path<? extends GroupStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupStatus(PathMetadata metadata, PathInits inits) {
        this(GroupStatus.class, metadata, inits);
    }

    public QGroupStatus(Class<? extends GroupStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroup(forProperty("group"), inits.get("group")) : null;
        this.groupLeader = inits.isInitialized("groupLeader") ? new QMember(forProperty("groupLeader"), inits.get("groupLeader")) : null;
        this.groupMember = inits.isInitialized("groupMember") ? new QMember(forProperty("groupMember"), inits.get("groupMember")) : null;
    }

}

