package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFunding is a Querydsl query type for Funding
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFunding extends EntityPathBase<Funding> {

    private static final long serialVersionUID = -1036414781L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFunding funding = new QFunding("funding");

    public final QBaseTime _super = new QBaseTime(this);

    public final BooleanPath closed = createBoolean("closed");

    public final BooleanPath completed = createBoolean("completed");

    //inherited
    public final StringPath createdDate = _super.createdDate;

    public final NumberPath<Long> currentFundingAmount = createNumber("currentFundingAmount", Long.class);

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath fundingAccount = createString("fundingAccount");

    public final NumberPath<Integer> fundingCategoryCode = createNumber("fundingCategoryCode", Integer.class);

    public final NumberPath<Long> fundingId = createNumber("fundingId", Long.class);

    public final QGroup group;

    //inherited
    public final StringPath lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final QProduct product;

    public final NumberPath<Long> totalFundingAmount = createNumber("totalFundingAmount", Long.class);

    public QFunding(String variable) {
        this(Funding.class, forVariable(variable), INITS);
    }

    public QFunding(Path<? extends Funding> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFunding(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFunding(PathMetadata metadata, PathInits inits) {
        this(Funding.class, metadata, inits);
    }

    public QFunding(Class<? extends Funding> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroup(forProperty("group"), inits.get("group")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

