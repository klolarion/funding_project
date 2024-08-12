package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroup is a Querydsl query type for Group
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroup extends EntityPathBase<Group> {

    private static final long serialVersionUID = -201359931L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroup group = new QGroup("group1");

    public final QBaseTime _super = new QBaseTime(this);

    //inherited
    public final StringPath createdDate = _super.createdDate;

    public final BooleanPath groupActive = createBoolean("groupActive");

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final QMember groupLeader;

    public final StringPath groupName = createString("groupName");

    //inherited
    public final StringPath lastModifiedDate = _super.lastModifiedDate;

    public QGroup(String variable) {
        this(Group.class, forVariable(variable), INITS);
    }

    public QGroup(Path<? extends Group> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroup(PathMetadata metadata, PathInits inits) {
        this(Group.class, metadata, inits);
    }

    public QGroup(Class<? extends Group> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupLeader = inits.isInitialized("groupLeader") ? new QMember(forProperty("groupLeader"), inits.get("groupLeader")) : null;
    }

}

