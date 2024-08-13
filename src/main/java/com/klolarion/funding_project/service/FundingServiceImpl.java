package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.FundingListDto;
import com.klolarion.funding_project.dto.JoinFundingDto;
import com.klolarion.funding_project.repository.FundingRepository;
import com.klolarion.funding_project.repository.PaymentRepository;
import com.klolarion.funding_project.service.blueprint.FundingService;
import com.klolarion.funding_project.util.CurrentMember;
import com.klolarion.funding_project.util.ProgressCalculator;
import com.klolarion.funding_project.util.RandomAccountGenerator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FundingServiceImpl implements FundingService {
    private final PaymentRepository paymentRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final RandomAccountGenerator randomAccountGenerator;
    private final FundingRepository fundingRepository;
    private final CurrentMember currentMember;
    private final ProgressCalculator calculator;

    @Override
    public List<FundingListDto> allFundingList() {
        QFunding qFunding = QFunding.funding;
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;

        List<FundingListDto> fundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0).as("progress"),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")))
                .from(qFunding)
                .join(qFunding.member, qMember)   // Member와 조인
                .join(qFunding.product, qProduct) // Product와 조인
                .fetch();
        em.flush();
        em.clear();
        return fundingListDtos;
    }

    @Override
    public List<FundingListDto> myFundingList() {
        Member member = currentMember.getMember();
        QFunding qFunding = QFunding.funding;

        List<FundingListDto> myFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0).as("progress"),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")
                ))
                .from(qFunding)
                .where(
                        qFunding.member.memberId.eq(member.getMemberId())  // 현재 로그인한 사용자와 관련된 펀딩만 조회
                )
                .fetch();
        em.flush();
        em.clear();
        return myFundingListDtos;
    }

    public FundingListDto fundingDetail(Long fundingId){
        QFunding qFunding = QFunding.funding;

        FundingListDto fundingListDto = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.group.groupId,
                        qFunding.group.groupName,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0).as("progress"),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")
                ))
                .from(qFunding)
                .where(qFunding.fundingId.eq(fundingId))  // 특정 fundingId에 해당하는 펀딩 조회
                .fetchOne();
        em.flush();
        em.clear();
        return fundingListDto;

    }


    /*내가속한 그룹별 펀딩리스트*/
    @Override
    public Map<String, List<FundingListDto>> allFundingListByGroup(Long groupId) {
        Member member = currentMember.getMember();
        QFunding qFunding = QFunding.funding;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QGroup qGroup = QGroup.group;


        List<FundingListDto> allGroupFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qGroup.groupId,
                        qGroup.groupName,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")
                ))
                .from(qFunding)
                .join(qFunding.group, qGroup) // Funding과 Group 간의 관계를 조인
                .join(qGroupStatus).on(qGroupStatus.group.eq(qGroup)) // GroupStatus와 Group 간의 관계를 조인
                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId())) // 멤버가 속한 그룹만 필터링
                .fetch();

        // 결과를 그룹 이름을 key로 하는 Map으로 변환
        Map<String, List<FundingListDto>> groupedFundingMap = allGroupFundingListDtos.stream()
                .collect(Collectors.groupingBy(FundingListDto::getGroupName));

        return groupedFundingMap;
    }


    /* 내 친구들의 친구별 펀딩 리스트를 반환 */
    @Override
    public Map<String, List<FundingListDto>> allFundingListByMyFriend(Long memberId) {
        QFunding qFunding = QFunding.funding;
        QFriend qFriend = QFriend.friend;
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;

        // 내 친구들이 가진 펀딩 데이터를 조회하여 FundingListDto 리스트로 변환
        List<FundingListDto> allFriendFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qMember.memberId,
                        qGroup.groupId,
                        qGroup.groupName,
                        qMember.memberName,
                        qProduct.productId,
                        qProduct.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        qFunding.completed,
                        qFunding.closed,
                        qFunding.deleted
                ))
                .from(qFunding)
                .join(qFunding.member, qMember)  // Funding과 Member 간의 관계 조인
                .join(qFunding.group, qGroup)    // Funding과 Group 간의 관계 조인
                .join(qFunding.product, qProduct) // Funding과 Product 간의 관계 조인
                .join(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))  // 친구 관계 조인
                .where(qFriend.deleted.isFalse()
                        .and(qFriend.banned.isFalse()) // 친구 관계가 수락된 상태인지 확인
                        .and(qFriend.requester.memberId.eq(memberId).or(qFriend.accepter.memberId.eq(memberId)))  // 현재 사용자가 친구 관계에 포함되어 있는지 확인
                )
                .fetch();

        // 결과를 친구 이름을 key로 하는 Map으로 변환
        Map<String, List<FundingListDto>> groupedFundingMap = allFriendFundingListDtos.stream()
                .collect(Collectors.groupingBy(FundingListDto::getMemberName)); // 친구 이름으로 그룹화

        return groupedFundingMap;
    }


    @Override
    public List<FundingListDto> fundingListByMyFriend(Long friendId) {
        Member member = currentMember.getMember();
        QFunding qFunding = QFunding.funding;
        QFriend qFriend = QFriend.friend;
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;  // 그룹 정보도 사용한다고 가정

        List<FundingListDto> friendFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qMember.memberId,
                        qGroup.groupId,
                        qGroup.groupName,
                        qMember.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")
                ))
                .from(qFunding)
                .join(qFunding.member, qMember)
                .join(qFunding.group, qGroup) // 가정: Funding과 Group 간의 관계 조인
                .join(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))
                .where(qFriend.friendId.eq(friendId)
                        .and(qFriend.deleted.isFalse())
                        .and(qFriend.banned.isFalse())
                        .and(qFriend.requester.memberId.eq(member.getMemberId()).or(qFriend.accepter.memberId.eq(member.getMemberId())))
                )
                .fetch();

        return friendFundingListDtos;
    }

    /*내가 속한 그룹의 펀딩 리스트*/
    @Override
    public List<FundingListDto> fundingListByMyGroup(Long groupId) {
        Member member = currentMember.getMember();
        QFunding qFunding = QFunding.funding;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;


        List<FundingListDto> allGroupFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0).as("progress"),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        new CaseBuilder()
                                .when(qFunding.deleted.isTrue())
                                .then("삭제")
                                .when(qFunding.completed.isTrue())
                                .then("완료")
                                .when(qFunding.closed.isTrue())
                                .then("중단")
                                .otherwise("펀딩중").as("status")
                ))
                .from(qFunding)
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .join(qGroup).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .where(
                        qFunding.member.memberId.eq(member.getMemberId())
                                .and(qGroup.groupId.eq(qGroupStatus.group.groupId))
                )
                .fetch();
        em.flush();
        em.clear();
        return allGroupFundingListDtos;
    }

    @Override
    public List<FundingListDto> fundingListByProduct(Long productId) {
        Member member = currentMember.getMember();
        QFunding qFunding = QFunding.funding;
        QFriend qFriend = QFriend.friend;
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;

        List<FundingListDto> fundingListByProduct = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qMember.memberId,
                        qGroup.groupId,
                        qGroup.groupName,
                        qMember.memberName,
                        qProduct.productId,
                        qProduct.productName,
                        qFunding.currentFundingAmount.doubleValue().divide(qFunding.totalFundingAmount.doubleValue()).multiply(100).coalesce(0.0),
                        qFunding.totalFundingAmount,
                        qFunding.currentFundingAmount,
                        qFunding.fundingAccount,
                        qFunding.completed,
                        qFunding.closed,
                        qFunding.deleted
                ))
                .from(qFunding)
                .join(qFunding.member, qMember)  // Funding과 Member 간의 관계 조인
                .join(qFunding.group, qGroup)    // Funding과 Group 간의 관계 조인
                .join(qFunding.product, qProduct) // Funding과 Product 간의 관계 조인
                .join(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))  // 친구 관계 조인
                .where(qProduct.productId.eq(productId)   // 특정 productId에 해당하는 펀딩만 필터링
                        .and(qFriend.deleted.isFalse())
                        .and(qFriend.banned.isFalse())
                        .and(qFriend.requester.memberId.eq(member.getMemberId()).or(qFriend.accepter.memberId.eq(member.getMemberId()))))
                .fetch();

        return fundingListByProduct;
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
                .leftJoin(qGroup).on((groupId != null ? qGroup.groupId.eq(groupId) : Expressions.TRUE))
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
