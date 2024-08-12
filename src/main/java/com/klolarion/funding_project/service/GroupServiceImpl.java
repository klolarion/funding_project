package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.GroupDto;
import com.klolarion.funding_project.repository.GroupRepository;
import com.klolarion.funding_project.repository.GroupStatusRepository;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
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
                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
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
    public Group startGroup() {
        return null;
    }

    @Override
    public boolean inviteMember(Long groupId, Long memberId) {
        return false;
    }

    @Override
    public boolean acceptInviteRequest(Long groupId, Long memberId) {
        return false;
    }

    @Override
    public boolean requestToGroup(Long groupId) {
        return false;
    }

    @Override
    public boolean acceptMemberRequest(Long groupId, Long memberId) {
        return false;
    }

    @Override
    public boolean exileMember(Long groupId, Long memberId) {
        return false;
    }

    @Override
    public boolean exitGroup(Long groupId, Long memberId) {
        return false;
    }

    @Override
    public boolean banMember(Long groupId, Long memberId) {
        return false;
    }
}
