package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.travel.TravelDto;
import com.klolarion.funding_project.repository.TravelRepository;
import com.klolarion.funding_project.service.blueprint.TravelService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TravelServiceImpl implements TravelService {
    private final JPAQueryFactory query;
    private final EntityManager em;
    private final CurrentMember currentMember;
    private final TravelRepository travelRepository;


    @Override
    public List<Travel> allTravels() {
        QTravel qTravel = QTravel.travel;
        return query.selectFrom(qTravel).fetch();

    }

    @Override
    public List<Travel> myTravels() {
        QTravel qTravel = QTravel.travel;
        QFunding qFunding = QFunding.funding;
        Member member = currentMember.getMember();
        return query.selectFrom(qTravel)
                .leftJoin(qFunding).on(qTravel.travelId.eq(qFunding.travel.travelId))
                .where(qTravel.memberId.eq(member.getMemberId())
                        .and(qFunding.travel.travelId.isNull()))  // Funding에 등록되지 않은 Travel
                .fetch();
    }

    @Override
    public Travel getTravel(Long travelId) {
        QTravel qTravel = QTravel.travel;
        return query.selectFrom(qTravel).where(qTravel.travelId.eq(travelId)).fetchOne();
    }

    @Override
    public Travel createTravel(TravelDto travelDto) {
        Member member = currentMember.getMember();
        //생성시 펀딩은 null
        Travel travel = new Travel(
                travelDto.getCity(),
                travelDto.getGroupId(),
                travelDto.getTravelName(),
                member.getMemberId(),
                null,
                travelDto.getPrice(),
                travelDto.getDescription(),
                travelDto.getStartDate(),
                travelDto.getEndDate()
        );
        return travelRepository.save(travel);
    }

    @Override
    public boolean modifyTravel(TravelDto travelDto) {
        QTravel qTravel = QTravel.travel;
        long result = query.update(qTravel)
                .set(qTravel.travelName, travelDto.getTravelName())
                .set(qTravel.city, travelDto.getCity())
                .set(qTravel.description, travelDto.getDescription())
                .set(qTravel.price, travelDto.getPrice())
                .set(qTravel.startDate, travelDto.getStartDate())
                .set(qTravel.endDate, travelDto.getEndDate())
                .where(qTravel.travelId.eq(travelDto.getTravelId()))
                .execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean deleteTravel(Long travelId) {
        QTravel qTravel = QTravel.travel;
        long result = query.delete(qTravel).where(qTravel.travelId.eq(travelId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 잠금 설정
                .execute();
        em.flush();
        em.clear();
        return result == 1L;
    }
}
