package com.klolarion.funding_project.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeMaster is a Querydsl query type for CodeMaster
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeMaster extends EntityPathBase<CodeMaster> {

    private static final long serialVersionUID = -937364695L;

    public static final QCodeMaster codeMaster = new QCodeMaster("codeMaster");

    public final QBaseTime _super = new QBaseTime(this);

    public final NumberPath<Integer> code = createNumber("code", Integer.class);

    public final NumberPath<Long> codeId = createNumber("codeId", Long.class);

    //inherited
    public final StringPath createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    //inherited
    public final StringPath lastModifiedDate = _super.lastModifiedDate;

    public final StringPath reference = createString("reference");

    public QCodeMaster(String variable) {
        super(CodeMaster.class, forVariable(variable));
    }

    public QCodeMaster(Path<? extends CodeMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeMaster(PathMetadata metadata) {
        super(CodeMaster.class, metadata);
    }

}

