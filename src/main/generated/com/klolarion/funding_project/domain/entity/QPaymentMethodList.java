package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentMethodList is a Querydsl query type for PaymentMethodList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentMethodList extends EntityPathBase<PaymentMethodList> {

    private static final long serialVersionUID = -1820841461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentMethodList paymentMethodList = new QPaymentMethodList("paymentMethodList");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath mainPayment = createBoolean("mainPayment");

    public final QMember member;

    public final QPaymentMethod paymentMethod;

    public QPaymentMethodList(String variable) {
        this(PaymentMethodList.class, forVariable(variable), INITS);
    }

    public QPaymentMethodList(Path<? extends PaymentMethodList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentMethodList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentMethodList(PathMetadata metadata, PathInits inits) {
        this(PaymentMethodList.class, metadata, inits);
    }

    public QPaymentMethodList(Class<? extends PaymentMethodList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.paymentMethod = inits.isInitialized("paymentMethod") ? new QPaymentMethod(forProperty("paymentMethod")) : null;
    }

}

