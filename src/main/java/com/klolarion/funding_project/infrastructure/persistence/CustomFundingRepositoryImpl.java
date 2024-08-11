package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.interfaces.dto.JoinFundingDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomFundingRepositoryImpl implements CustomFundingRepository{
    private final JPAQueryFactory query;

    @Override
    public List<Funding> allFundingList() {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Funding> allFundingListByGroup(List<Long> groupId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.group.groupId.in(groupId)).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Funding> allFundingListByMyFriend(List<Long> memberId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.member.memberId.in(memberId)).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Funding> fundingListByMyFriend(Long friendId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.member.memberId.eq(friendId)).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Funding> fundingListByMyGroup(Long groupId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.group.groupId.eq(groupId)).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Funding> fundingListByProduct(Long productId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.product.productId.eq(productId)).fetchAll();
        return fundingJPAQuery.fetch();
    }

    @Override
    public List<Object> joinFunding(JoinFundingDto joinFundingDto) {
        Long fundingId = joinFundingDto.getFundingId();
        Long memberId = joinFundingDto.getMemberId();

        QFunding qFunding = QFunding.funding;
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;

        Tuple result = query.select(
                        qFunding,
                        qPaymentMethodList,
                        qPaymentMethod
                )
                .from(qFunding)
                .join(qPaymentMethodList).on(qPaymentMethodList.member.memberId.eq(memberId).and(qPaymentMethodList.mainPayment.isTrue()))
                .join(qPaymentMethod).on(qPaymentMethod.paymentMethodId.eq(qPaymentMethodList.paymentMethod.paymentMethodId))
                .where(qFunding.fundingId.eq(fundingId))
                .fetchOne();

        Funding funding = result.get(qFunding);
        PaymentMethod paymentMethod = result.get(qPaymentMethod);

        List<Object> list = new ArrayList<>();

        list.add(funding);
        list.add(paymentMethod);


        return list;

    }
}
