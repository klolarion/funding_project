package com.klolarion.funding_project.funding_test;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.repository.FundingRepository;
import com.klolarion.funding_project.util.RandomAccountGenerator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FundingTest {
    @Autowired
    private RandomAccountGenerator randomAccountGenerator;
    @Autowired
    private JPAQueryFactory query;
    @Autowired
    private FundingRepository fundingRepository;

    @Test
    @DisplayName("펀딩 생성 테스트")
    void createFunding() {
        Long productId = 1L;
        Long groupId = null;
        Long memberId = 1L;
        int fundingCategoryCode = 902;

        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;
        //전달받은 id로 각 객체 조회
        //그룹id가 null이면 조회하지 않음
        Tuple tuple = query.select(
                        qMember,
                        qProduct,
                        qGroup
                ).from(qMember)
                .join(qProduct).on(qProduct.productId.eq(productId))
                .leftJoin(qGroup).on(groupId != null ? qGroup.groupId.eq(groupId) : Expressions.FALSE.isTrue())
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        Member member = tuple.get(qMember);
        Product product = tuple.get(qProduct);
        Group group = tuple.get(qGroup);

        //상품 재고, 재입고여부, 판매종료여부 확인
        if (product.getStock() > 0 || product.isRestock() || !product.isSaleFinished()) {

            Funding funding = new Funding(
                    member,
                    product,
                    group,
                    product.getPrice(),
                    randomAccountGenerator.generateRandomAccount(),
                    fundingCategoryCode
            );

            Funding saved = fundingRepository.save(funding);
            System.out.println(saved.getFundingId());
        }
        // given

        // when

        // then
    }

}
