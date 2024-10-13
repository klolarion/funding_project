package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.group.GroupDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupStatusRepository groupStatusRepository;
    private final CurrentMember currentMember;
    private final JPAQueryFactory query;
    private final EntityManager em;

    // 내가 속하지않은 모든 그룹

    @Override
    public List<Member> invitedMembersToMyGroup(Long groupId){
        QMember qMember = QMember.member;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;

        List<Member> members = query.selectFrom(qMember)
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(qMember.memberId))
                .where(qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.invited.isTrue())
                        .and(qGroupStatus.requested.isFalse())
                        .and(qGroupStatus.accepted.isFalse()) // 그룹 상태에서 accepted가 true
                        .and(qGroupStatus.exited.isFalse()) // 그룹 상태에서 exited가 false
                        .and(qGroupStatus.banned.isFalse())) // 그룹 상태에서 banned가 false)
                .fetch();
        em.flush();
        em.clear();
        return members;

    }

    @Override
    public List<Member> requestedMembersToMyGroup(Long groupId){
        QMember qMember = QMember.member;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;

        List<Member> members = query.selectFrom(qMember)
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(qMember.memberId))
                .where(qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.requested.isTrue())
                        .and(qGroupStatus.invited.isFalse())
                        .and(qGroupStatus.accepted.isFalse()) // 그룹 상태에서 accepted가 true
                        .and(qGroupStatus.exited.isFalse()) // 그룹 상태에서 exited가 false
                        .and(qGroupStatus.banned.isFalse())) // 그룹 상태에서 banned가 false)
                .fetch();
        em.flush();
        em.clear();
        return members;
    }


    /*내가 그룹장인 그룹 전부 조회*/
    public List<GroupDto> myLeaderGroups() {
        Member member = currentMember.getMember();
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                                .from(qGroupStatus)
                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId)),
                        qGroup.groupCategoryCode
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


    //illigal argument excep
    /*내가 속한 그룹*/
    @Override
    public List<GroupDto> myGroups() {
        Member member = currentMember.getMember();
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                                .from(qGroupStatus)
                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId)),
                        qGroup.groupCategoryCode
                ))
                .from(qGroup)
                .leftJoin(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
                .fetch();

        em.flush();
        em.clear();
        return groups;
    }

    /*내가 속하지 않은 모든 그룹*/
    @Override
    public List<GroupDto> allGroupExceptMy(){
        Member member = currentMember.getMember();
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                        .from(qGroupStatus)
                        .where(qGroupStatus.group.groupId.eq(qGroup.groupId)),
                        qGroup.groupCategoryCode
                        ))
                .from(qGroup)
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .where(
                        qGroup.groupLeader.memberId.ne(member.getMemberId()) // 내가 그룹 리더가 아닌 그룹
                                .and(
                                        JPAExpressions.select(qGroupStatus.groupMember.memberId)
                                                .from(qGroupStatus)
                                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId)
                                                        .and(qGroupStatus.groupMember.memberId.eq(member.getMemberId())))
                                                .notExists() // 내가 그룹에 속하지 않은 그룹
                                )
                )
                .fetch();

        em.flush();
        em.clear();
        return groups;
    }

    @Override
    public List<GroupDto> allGroups(){
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QMember qMember = QMember.member;

        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        JPAExpressions.select(qGroupStatus.countDistinct())
                                .from(qGroupStatus)
                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId)),
                        qGroup.groupCategoryCode
                ))
                .from(qGroup)
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .fetch();

        em.flush();
        em.clear();
        return groups;
    }

    @Override
    public List<Member> groupMembers(Long groupId){
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QGroup qGroup = QGroup.group;
        QMember qMember = QMember.member;

//        그룹아이디에맞는 그룹 검색
//        그룹아이디로 그룹상태 조인
//        그룹상태에서 조건필터링된 멤버 조회

        List<Member> groupMembers = query.selectFrom(qMember)
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(qMember.memberId)) // 그룹 상태와 멤버를 조인
                .join(qGroup).on(qGroup.groupId.eq(qGroupStatus.group.groupId)) // 그룹 상태와 그룹을 조인
                .where(qGroup.groupId.eq(groupId) // 그룹 아이디가 일치하는지 확인
                        .and(qGroupStatus.accepted.isTrue()) // 그룹 상태에서 accepted가 true
                        .and(qGroupStatus.exited.isFalse()) // 그룹 상태에서 exited가 false
                        .and(qGroupStatus.banned.isFalse())) // 그룹 상태에서 banned가 false
                .fetch();
        em.flush();
        em.clear();
        return groupMembers;
    }

    @Override
    public GroupDto groupDetail(Long groupId) {
        Long groupIdTmp = 0L;
        Member member = currentMember.getMember();
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QFunding qFunding = QFunding.funding;

        // QueryDSL 쿼리 작성
        List<Tuple> results = query.select(
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        qGroupStatus.group.countDistinct().as("groupMemberCount"),
                        qFunding.fundingId,  // 각 필드를 명시적으로 추가
                        qGroup.groupCategoryCode
                )
                .from(qGroup)
                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .leftJoin(qFunding).on(qFunding.group.groupId.eq(qGroup.groupId))
                .where(
                        qGroupStatus.groupLeader.memberId.eq(member.getMemberId()) // `group_leader_id = :memberId` 조건
                                .and(qGroup.groupId.eq(groupId)) // `g.group_id = 2` 조건
                )
                .groupBy(
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName,
                        qGroup.groupName,
                        qFunding.fundingId // 명시적으로 그룹화
                )
                .fetch();

        Map<Long, GroupDto> groupDtoMap = new HashMap<>();

        for (Tuple tuple : results) {
            groupIdTmp = tuple.get(qGroup.groupId);

            GroupDto groupDto = groupDtoMap.get(groupIdTmp);
            if (groupDto == null) {
                groupDto = new GroupDto();
                groupDto.setGroupId(groupIdTmp);
                groupDto.setGroupLeaderId(tuple.get(qGroup.groupLeader.memberId));
                groupDto.setGroupLeaderName(tuple.get(qMember.nickName.as("groupLeaderName")));
                groupDto.setGroupName(tuple.get(qGroup.groupName));
                groupDto.setGroupMemberCount(tuple.get(qGroupStatus.group.countDistinct().as("groupMemberCount")));
                groupDto.setGroupCategoryCode(tuple.get(qGroup.groupCategoryCode));
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
    public GroupDto groupInfoDetail(Long groupId) {
        Long groupIdTmp = 0L;
        QMember qMember = QMember.member;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QFunding qFunding = QFunding.funding;

        // QueryDSL 쿼리 작성
        List<Tuple> results = query.select(
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName.as("groupLeaderName"),
                        qGroup.groupName,
                        qGroupStatus.group.countDistinct().as("groupMemberCount"),
                        qFunding.fundingId,  // 각 필드를 명시적으로 추가
                        qGroup.groupCategoryCode
                )
                .from(qGroup)
                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
                .leftJoin(qFunding).on(qFunding.group.groupId.eq(qGroup.groupId))
                .where(
//                        qGroupStatus.groupLeader.memberId.eq(member.getMemberId()) // `group_leader_id = :memberId` 조건
                                qGroup.groupId.eq(groupId)// `g.group_id = 2` 조건
                )
                .groupBy(
                        qGroup.groupId,
                        qGroup.groupLeader.memberId,
                        qMember.nickName,
                        qGroup.groupName,
                        qFunding.fundingId // 명시적으로 그룹화
                )
                .fetch();

        Map<Long, GroupDto> groupDtoMap = new HashMap<>();

        for (Tuple tuple : results) {
            groupIdTmp = tuple.get(qGroup.groupId);

            GroupDto groupDto = groupDtoMap.get(groupIdTmp);
            if (groupDto == null) {
                groupDto = new GroupDto();
                groupDto.setGroupId(groupIdTmp);
                groupDto.setGroupLeaderId(tuple.get(qGroup.groupLeader.memberId));
                groupDto.setGroupLeaderName(tuple.get(qMember.nickName.as("groupLeaderName")));
                groupDto.setGroupName(tuple.get(qGroup.groupName));
                groupDto.setGroupMemberCount(tuple.get(qGroupStatus.group.countDistinct().as("groupMemberCount")));
                groupDto.setGroupCategoryCode(tuple.get(qGroup.groupCategoryCode));
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
    public Group startGroup(String groupName, String groupCategory) {
        Member member = currentMember.getMember();
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        CodeMaster codeMaster = query.selectFrom(qCodeMaster).where(qCodeMaster.description.eq(groupCategory)).fetchOne();
        Group group = new Group(member, groupName, codeMaster.getCode());
        GroupStatus groupStatus = new GroupStatus(
                group, member, member, false, false, true
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
                group, groupLeader, member, true, false, false
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
                member,
                false,
                true,
                false
        );
        return groupStatusRepository.save(groupStatus);

    }

    @Override
    public boolean acceptMemberRequest(Long groupId, Long memberId) {
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;

        long result = query.update(qGroupStatus)
                .set(qGroupStatus.accepted, true)
                .where(qGroupStatus.group.groupId.eq(groupId)
                        .and(qGroupStatus.groupMember.memberId.eq(memberId)
                                .and(qGroupStatus.accepted.isFalse())
                                .and(qGroupStatus.requested.isTrue())
                                .and(qGroupStatus.exited.isFalse())
                                .and(qGroupStatus.banned.isFalse()))).execute();
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

    @Override
    public boolean groupNameCheck(String groupName) {
        QGroup qGroup = QGroup.group;
        boolean exists = query
                .selectOne()
                .from(qGroup)
                .where(qGroup.groupName.eq(groupName))
                .fetchFirst() != null;
        return !exists; //그룹명이 존재하면 false리턴
    }
}
