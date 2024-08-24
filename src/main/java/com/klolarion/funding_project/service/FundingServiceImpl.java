package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import com.klolarion.funding_project.repository.FundingRepository;
import com.klolarion.funding_project.repository.PaymentRepository;
import com.klolarion.funding_project.service.blueprint.FundingService;
import com.klolarion.funding_project.util.CurrentMember;
import com.klolarion.funding_project.util.RandomAccountGenerator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FundingServiceImpl implements FundingService {
    private final PaymentRepository paymentRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final RandomAccountGenerator randomAccountGenerator;
    private final FundingRepository fundingRepository;
    private final CurrentMember currentMember;

    /*전체 펀딩 조회*/
    @Override
    public List<FundingListDto> allFundingList() {
        QFunding qFunding = QFunding.funding;
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QTravel qTravel = QTravel.travel;

        List<FundingListDto> fundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
                        // progress를 소수점 첫 번째 자리까지만 표시
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)",
                                qFunding.currentFundingAmount.doubleValue()
                                        .divide(qFunding.totalFundingAmount.doubleValue())
                                        .multiply(100)
                                        .coalesce(0.0)
                        ).as("progress"),
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
                .leftJoin(qFunding.product, qProduct) // Product와 조인
                .leftJoin(qFunding.travel, qTravel) // Travel과 조인
                .fetch();
        em.flush();
        em.clear();
        return fundingListDtos;
    }

    /*내 펀딩 리스트 조회*/
    @Override
    public List<FundingListDto> myFundingList() {
        QFunding qFunding = QFunding.funding;
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QTravel qTravel = QTravel.travel;
        Member member = currentMember.getMember();

        List<FundingListDto> myFundingListDtos = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
                        // progress를 소수점 첫 번째 자리까지만 표시
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)",
                                qFunding.currentFundingAmount.doubleValue()
                                        .divide(qFunding.totalFundingAmount.doubleValue())
                                        .multiply(100)
                                        .coalesce(0.0)
                        ).as("progress"),
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
                .join(qFunding.member, qMember)   // Member와 조인
                .leftJoin(qFunding.product, qProduct) // Product와 조인
                .leftJoin(qFunding.travel, qTravel) // Travel과 조인
                .where(
                        qFunding.member.memberId.eq(member.getMemberId())  // 현재 로그인한 사용자와 관련된 펀딩만 조회
                )
                .fetch();
        em.flush();
        em.clear();
        return myFundingListDtos;
    }

    public FundingListDto fundingDetail(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        QProduct qProduct = QProduct.product;
        QTravel qTravel = QTravel.travel;
        QGroup qGroup = QGroup.group;
        FundingListDto fundingListDto = query.select(Projections.constructor(FundingListDto.class,
                        qFunding.fundingId,
                        qFunding.member.memberId,
                        qFunding.group.groupId,
                        qFunding.group.groupName,
//                        new CaseBuilder()
//                        .when(qFunding.group.groupId.isNull()).then(Expressions.constant(0L))  // 그룹 ID가 null인 경우 null로 처리
//                        .otherwise(qFunding.group.groupId),  // 그룹 ID가 null이 아닌 경우
//                    new CaseBuilder()
//                        .when(qFunding.group.groupName.isNull()).then(Expressions.constant(""))  // 그룹 이름이 null인 경우 빈 문자열로 처리
//                        .otherwise(qFunding.group.groupName),
                        qFunding.member.memberName,
                        qFunding.product.productId,
                        qFunding.product.productName,
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
                        // progress를 소수점 첫 번째 자리까지만 표시
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)",
                                qFunding.currentFundingAmount.doubleValue()
                                        .divide(qFunding.totalFundingAmount.doubleValue())
                                        .multiply(100)
                                        .coalesce(0.0)
                        ).as("progress"),
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
                .distinct()
                .from(qFunding)
                .leftJoin(qFunding.product, qProduct) // Product와 조인
                .leftJoin(qFunding.travel, qTravel) // Travel과 조인
                .leftJoin(qFunding.group, qGroup) // Group과 조인
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
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
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
                .leftJoin(qFunding.group, qGroup) // Funding과 Group 간의 관계를 조인
                .leftJoin(qGroupStatus).on(qGroupStatus.group.eq(qGroup)) // GroupStatus와 Group 간의 관계를 조인
                .where(qFunding.group.groupId.eq(groupId))
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
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
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
                .leftJoin(qFunding.group, qGroup)    // Funding과 Group 간의 관계 조인
                .join(qFunding.product, qProduct) // Funding과 Product 간의 관계 조인
                .leftJoin(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))  // 친구 관계 조인
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
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
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
                .leftJoin(qFunding.group, qGroup) // 가정: Funding과 Group 간의 관계 조인
                .leftJoin(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))
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
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
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
                .leftJoin(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .leftJoin(qGroup).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
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
                        qFunding.travel.travelId,
                        qFunding.travel.travelName,
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
                .leftJoin(qFunding.group, qGroup)    // Funding과 Group 간의 관계 조인
                .leftJoin(qFunding.product, qProduct) // Funding과 Product 간의 관계 조인
                .leftJoin(qFriend).on(qFriend.requester.eq(qMember).or(qFriend.accepter.eq(qMember)))  // 친구 관계 조인
                .where(qProduct.productId.eq(productId)   // 특정 productId에 해당하는 펀딩만 필터링
                        .and(qFriend.deleted.isFalse())
                        .and(qFriend.banned.isFalse())
                        .and(qFriend.requester.memberId.eq(member.getMemberId()).or(qFriend.accepter.memberId.eq(member.getMemberId()))))
                .fetch();

        return fundingListByProduct;
    }

    @Override
    public Funding createFunding(Long memberId, Long productId, Long travelId, Long groupId) {
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        QTravel qTravel = QTravel.travel;
        int fundingCategoryCode = productId != null ? 901 : 902;
        //전달받은 id로 각 객체 조회
        Tuple tuple = query.select(
                        qMember,
                        qProduct,
                        qGroup,
                        qTravel,
                        qCodeMaster
                ).from(qMember)
                .leftJoin(qProduct).on(productId != null ? qProduct.productId.eq(productId) : qProduct.productId.isNull())
                .leftJoin(qTravel).on(travelId != null ? qTravel.travelId.eq(travelId) : qTravel.travelId.isNull())
                .join(qCodeMaster).on(qCodeMaster.code.eq(fundingCategoryCode))
                .leftJoin(qGroup).on(groupId != null ? qGroup.groupId.eq(groupId) : Expressions.FALSE.isTrue())
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        Member member = tuple.get(qMember);
        Product product = tuple.get(qProduct);
        Group group = tuple.get(qGroup);
        CodeMaster codeMaster = tuple.get(qCodeMaster);
        Travel travel = tuple.get(qTravel);

        if (travel != null) {
            Funding funding = new Funding(
                    member,
                    null,
                    travel,
                    group,
                    travel.getPrice(),
                    randomAccountGenerator.generateRandomAccount(),
                    codeMaster.getCode()
            );

            Funding saved = fundingRepository.save(funding);
            em.flush();
            em.clear();
            return saved;
        }

        //상품 재고, 재입고여부, 판매종료여부 확인
        if (product.getStock() > 0 || product.isRestock() || !product.isSaleFinished()) {

            Funding funding = new Funding(
                    member,
                    product,
                    null,
                    group,
                    product.getPrice(),
                    randomAccountGenerator.generateRandomAccount(),
                    codeMaster.getCode()
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
    public Funding createFundingApi(Long productId, Long travelId, Long groupId) {
        //결제수단이 없으면
        Member m = currentMember.getMember();
        QMember qMember = QMember.member;
        QProduct qProduct = QProduct.product;
        QGroup qGroup = QGroup.group;
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        QTravel qTravel = QTravel.travel;
        int fundingCategoryCode = productId != null ? 901 : 902;
        //전달받은 id로 각 객체 조회
        Tuple tuple = query.select(
                        qMember,
                        qProduct,
                        qGroup,
                        qTravel,
                        qCodeMaster
                ).from(qMember)
                .leftJoin(qProduct).on(qProduct.productId.eq(productId))
                .leftJoin(qTravel).on(qTravel.travelId.eq(travelId))
                .join(qCodeMaster).on(qCodeMaster.code.eq(fundingCategoryCode))
                .leftJoin(qGroup).on(groupId != null ? qGroup.groupId.eq(groupId) : Expressions.FALSE.isTrue())
                .where(qMember.memberId.eq(m.getMemberId()))
                .fetchOne();

        Member member = tuple.get(qMember);
        Product product = tuple.get(qProduct);
        Group group = tuple.get(qGroup);
        CodeMaster codeMaster = tuple.get(qCodeMaster);
        Travel travel = tuple.get(qTravel);

        if (travel != null) {
            Funding funding = new Funding(
                    member,
                    null,
                    travel,
                    group,
                    product.getPrice(),
                    randomAccountGenerator.generateRandomAccount(),
                    codeMaster.getCode()
            );

            Funding saved = fundingRepository.save(funding);
            em.flush();
            em.clear();
            return saved;
        }

        //상품 재고, 재입고여부, 판매종료여부 확인
        if (product.getStock() > 0 || product.isRestock() || !product.isSaleFinished()) {

            Funding funding = new Funding(
                    member,
                    product,
                    null,
                    group,
                    product.getPrice(),
                    randomAccountGenerator.generateRandomAccount(),
                    codeMaster.getCode()
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
    @Transactional
    public boolean completeFunding(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        long result = query.update(qFunding).set(qFunding.deleted, true).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    /*동시성제어 필요
     * 결제수단이 등록되지 않은경우 예외발생 -> 처리필요*/
    @Override
    @Transactional
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
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 락 설정
                .fetchOne();

        Funding funding = result.get(qFunding);
        PaymentMethod paymentMethod = result.get(qPaymentMethod);
        Member member = result.get(qMember);

        //금액조건이 유효하면 송금처리
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

            //결제목록 생성
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
