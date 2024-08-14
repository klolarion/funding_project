package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.GroupDto;
import com.klolarion.funding_project.repository.GroupRepository;
import com.klolarion.funding_project.repository.GroupStatusRepository;
import com.klolarion.funding_project.service.blueprint.GroupService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupStatusRepository groupStatusRepository;
    private final CurrentMember currentMember;
    private final JPAQueryFactory query;
    private final EntityManager em;


    /*내가 그룹장인 그룹 전부 조회*/
    @Override
    public List<GroupDto> myLeaderGroups() {
        Member member = currentMember.getMember();
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.memberName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                                .from(qGroupStatus)
                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId))
                ))
                .from(qGroup)
                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .fetch();

        em.flush();
        em.clear();
        return groups;
    }

    @Override
    public List<GroupDto> myGroups() {
        Member member = currentMember.getMember();
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.memberName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                                .from(qGroupStatus)
                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId))
                ))
                .from(qGroup)
                .leftJoin(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .fetch();

        em.flush();
        em.clear();
        return groups;
    }

    @Override
    public GroupDto groupDetail(Long groupId) {
        Long groupIdTmp = 0L;
        Member member = currentMember.getMember();
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QFunding qFunding = QFunding.funding;

        List<Tuple> results = query.select(
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.memberName.as("groupLeaderName"),
                        qGroup.groupName,
                        qGroupStatus.group.countDistinct().as("groupMemberCount"),
                        qFunding
                )
                .from(qGroup)
                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .leftJoin(qFunding).on(qFunding.group.groupId.eq(qGroup.groupId))
                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .groupBy(qGroup.groupId, qGroup.groupLeader.memberId, qMember.memberName, qGroup.groupName, qFunding.fundingId) // 필요한 필드들을 그룹화
                .fetch();

        Map<Long, GroupDto> groupDtoMap = new HashMap<>();

        for (Tuple tuple : results) {
            groupIdTmp = tuple.get(qGroup.groupId);

            GroupDto groupDto = groupDtoMap.get(groupIdTmp);
            if (groupDto == null) {
                groupDto = new GroupDto();
                groupDto.setGroupId(groupIdTmp);
                groupDto.setGroupLeaderId(tuple.get(qGroup.groupLeader.memberId));
                groupDto.setGroupLeaderName(tuple.get(qMember.memberName.as("groupLeaderName")));
                groupDto.setGroupName(tuple.get(qGroup.groupName));
                groupDto.setGroupMemberCount(tuple.get(qGroupStatus.group.countDistinct().as("groupMemberCount")));

                groupDto.setGroupFunding(new ArrayList<>());
                groupDtoMap.put(groupIdTmp, groupDto);
            }

            Funding funding = tuple.get(qFunding);
            if (funding != null) {
                groupDto.getGroupFunding().add(funding);
            }
        }

        GroupDto groupDto = groupDtoMap.isEmpty() ? null : groupDtoMap.values().iterator().next();

        em.flush();
        em.clear();

        return groupDto;
    }

    @Override
    public Group startGroup(String groupName) {
        Member member = currentMember.getMember();
        Group group = new Group(member, groupName);
        GroupStatus groupStatus = new GroupStatus(
                group, member, member
        );
        groupStatusRepository.save(groupStatus);
        return groupRepository.save(group);
    }

    @Override
    public GroupStatus inviteMember(Long groupId, Long memberId) {
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;

        Group group = query.selectFrom(qGroup).where(qGroup.groupId.eq(groupId)).fetchOne();
        Member groupLeader = query.selectFrom(qMember).where(qMember.memberId.eq(group.getGroupLeader().getMemberId())).fetchOne();
        Member member = query.selectFrom(qMember).where(qMember.memberId.eq(memberId)).fetchOne();

        GroupStatus groupStatus = new GroupStatus(
                group, groupLeader, member
        );
        return groupStatusRepository.save(groupStatus);
    }

    @Override
    public boolean acceptInviteRequest(Long groupStatusId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        long result = query.update(qGroupStatus).set(qGroupStatus.accepted, true).where(
                qGroupStatus.groupMember.memberId.eq(memberId)
                        .and(qGroupStatus.banned.isFalse())
                        .and(qGroupStatus.exited.isFalse())
        ).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public GroupStatus requestToGroup(Long groupId) {
        Member member = currentMember.getMember();
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;

        Group group = query.selectFrom(qGroup).where(qGroup.groupId.eq(groupId)).fetchOne();
        Member groupLeader = query.selectFrom(qMember).where(qMember.memberId.eq(group.getGroupLeader().getMemberId())).fetchOne();

        GroupStatus groupStatus = new GroupStatus(
                group,
                groupLeader,
                member
        );
        return groupStatusRepository.save(groupStatus);

    }

    @Override
    public boolean acceptMemberRequest(Long groupStatusId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;

        long result = query.update(qGroupStatus)
                .set(qGroupStatus.accepted, true)
                .where(qGroupStatus.groupStatusId.eq(groupStatusId)
                        .and(qGroupStatus.groupMember.memberId.eq(memberId))).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean exileMember(Long groupId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        long result = query.update(qGroupStatus)
                .set(qGroupStatus.exited, true)
                .set(qGroupStatus.banned, true)
                .where(
                        qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.groupMember.memberId.eq(memberId))
                        .and(qGroupStatus.accepted.isTrue())
        ).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean exitGroup(Long groupId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        long result = query.update(qGroupStatus).set(qGroupStatus.exited, true).where(
                qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.groupMember.memberId.eq(memberId))
                        .and(qGroupStatus.accepted.isTrue())
        ).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean banMember(Long groupId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        long result = query.update(qGroupStatus).set(qGroupStatus.banned, true).where(
                qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.groupMember.memberId.eq(memberId))
                        .and(qGroupStatus.accepted.isTrue())
        ).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }
}
