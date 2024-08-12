package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.JoinFundingDto;
import com.klolarion.funding_project.repository.FundingRepository;
import com.klolarion.funding_project.repository.PaymentRepository;
import com.klolarion.funding_project.util.RandomAccountGenerator;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FundingServiceImpl implements FundingService {
    private final PaymentRepository paymentRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final RandomAccountGenerator randomAccountGenerator;
    private final FundingRepository fundingRepository;

    @Override
    public List<Funding> allFundingList() {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;
    }

    @Override
    public List<Funding> allFundingListByGroup(List<Long> groupId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.group.groupId.in(groupId)).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;
    }

    @Override
    public List<Funding> allFundingListByMyFriend(List<Long> memberId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.member.memberId.in(memberId)).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;
    }

    @Override
    public List<Funding> fundingListByMyFriend(Long friendId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.member.memberId.eq(friendId)).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;
    }

    @Override
    public List<Funding> fundingListByMyGroup(Long groupId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.group.groupId.eq(groupId)).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;
    }

    @Override
    public List<Funding> fundingListByProduct(Long productId) {
        QFunding qFunding = QFunding.funding;
        JPAQuery<Funding> fundingJPAQuery = query.selectFrom(qFunding).where(qFunding.product.productId.eq(productId)).fetchAll();
        List<Funding> fundings = fundingJPAQuery.fetch();
        em.flush();
        em.clear();
        return fundings;

    }

    @Override
    public Funding createFunding(Long memberId, Long productId, Long groupId) {
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;
        Tuple tuple = query.select(
                        qMember,
                        qProduct,
                        qGroup
                ).from(qMember)
                .join(qProduct).on(qProduct.productId.eq(productId))
                .join(qGroup).on(qGroup.groupId.eq(groupId))
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
                    randomAccountGenerator.generateRandomAccount()
            );

            Funding saved = fundingRepository.save(funding);
            em.flush();
            em.clear();
            return saved;
        }
        em.flush();
        em.clear();
        return null;
    }

    @Override
    public boolean completeFunding(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        long result = query.update(qFunding).set(qFunding.deleted, true).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean joinFunding(JoinFundingDto joinFundingDto) {

        Long fundingId = joinFundingDto.getFundingId();
        Long memberId = joinFundingDto.getMemberId();
        Long amount = joinFundingDto.getAmount();

        QFunding qFunding = QFunding.funding;
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        QMember qMember = QMember.member;


        //조건에 해당하는 펀딩, 결제수단 객체 조회
        Tuple result = query.select(
                        qFunding,
                        qMember,
                        qPaymentMethodList,
                        qPaymentMethod
                )
                .from(qFunding)
                .join(qPaymentMethodList).on(qPaymentMethodList.member.memberId.eq(memberId).and(qPaymentMethodList.mainPayment.isTrue()))
                .join(qPaymentMethod).on(qPaymentMethod.paymentMethodId.eq(qPaymentMethodList.paymentMethod.paymentMethodId))
                .join(qMember).on(qMember.memberId.eq(memberId))
                .where(qFunding.fundingId.eq(fundingId))
                .fetchOne();

        Funding funding = result.get(qFunding);
        PaymentMethod paymentMethod = result.get(qPaymentMethod);
        Member member = result.get(qMember);


        //금액조건이 유효하면 송금처리 후 결제목록 생성
        if (paymentMethod.getAvailableAmount() >= amount &&
                funding.getTotalFundingAmount() - funding.getCurrentFundingAmount() >= amount) {

            long executeF = query.update(qFunding)
                    .set(qFunding.currentFundingAmount, funding.getCurrentFundingAmount() + amount)
                    .where(qFunding.fundingId.eq(fundingId))
                    .execute();

            long executeP = query.update(qPaymentMethod)
                    .set(qPaymentMethod.availableAmount, paymentMethod.getAvailableAmount() - amount)
                    .where(qPaymentMethod.paymentMethodId.eq(paymentMethod.getPaymentMethodId()))
                    .execute();

            Payment payment = new Payment(
                    member,
                    paymentMethod,
                    amount,
                    true
            );
            if (executeF == 1 && executeP == 1) {
                Payment saved = paymentRepository.save(payment);
                //저장과정에서 오류가 생기면 completed를 false로 저장.
                if (saved == null) {
                    payment.setCompletedFalse();
                    paymentRepository.save(payment);
                    em.flush();
                    em.clear();
                    return false;
                }
                em.flush();
                em.clear();
                return true;
            }
        }
        em.flush();
        em.clear();
        return false;
    }
}
