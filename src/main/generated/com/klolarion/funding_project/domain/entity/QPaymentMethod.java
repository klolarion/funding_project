package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentMethod is a Querydsl query type for PaymentMethod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentMethod extends EntityPathBase<PaymentMethod> {

    private static final long serialVersionUID = -610487347L;

    public static final QPaymentMethod paymentMethod = new QPaymentMethod("paymentMethod");

    public final QBaseTime _super = new QBaseTime(this);

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Long> availableAmount = createNumber("availableAmount", Long.class);

    //inherited
    public final StringPath createdDate = _super.createdDate;

    //inherited
    public final StringPath lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> paymentCode = createNumber("paymentCode", Integer.class);

    public final NumberPath<Long> paymentMethodId = createNumber("paymentMethodId", Long.class);

    public final StringPath paymentName = createString("paymentName");

    public QPaymentMethod(String variable) {
        super(PaymentMethod.class, forVariable(variable));
    }

    public QPaymentMethod(Path<? extends PaymentMethod> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentMethod(PathMetadata metadata) {
        super(PaymentMethod.class, metadata);
    }

}

