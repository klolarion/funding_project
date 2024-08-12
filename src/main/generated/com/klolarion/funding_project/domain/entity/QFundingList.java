package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFundingList is a Querydsl query type for FundingList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFundingList extends EntityPathBase<FundingList> {

    private static final long serialVersionUID = -170812415L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFundingList fundingList = new QFundingList("fundingList");

    public final QBaseTime _super = new QBaseTime(this);

    public final BooleanPath completed = createBoolean("completed");

    //inherited
    public final StringPath createdDate = _super.createdDate;

    public final NumberPath<Long> currentFundingAmount = createNumber("currentFundingAmount", Long.class);

    public final BooleanPath finished = createBoolean("finished");

    public final QFunding funding;

    public final NumberPath<Long> fundingListId = createNumber("fundingListId", Long.class);

    //inherited
    public final StringPath lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final QProduct product;

    public final StringPath productName = createString("productName");

    public final NumberPath<Long> totalFundingAmount = createNumber("totalFundingAmount", Long.class);

    public QFundingList(String variable) {
        this(FundingList.class, forVariable(variable), INITS);
    }

    public QFundingList(Path<? extends FundingList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFundingList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFundingList(PathMetadata metadata, PathInits inits) {
        this(FundingList.class, metadata, inits);
    }

    public QFundingList(Class<? extends FundingList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funding = inits.isInitialized("funding") ? new QFunding(forProperty("funding"), inits.get("funding")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

