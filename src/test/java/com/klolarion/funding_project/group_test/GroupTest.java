//package com.klolarion.funding_project.group_test;
//
//import com.klolarion.funding_project.domain.entity.*;
//import com.klolarion.funding_project.dto.group.GroupDto;
//import com.klolarion.funding_project.util.CurrentMember;
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.JPAExpressions;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//public class GroupTest {
//
//    @Autowired
//    private CurrentMember currentMember;
//    @Autowired
//    private JPAQueryFactory query;
//
//    @Test
//    @DisplayName("그룹 정보 조회")
//    void groupDetail() {
//        Long groupIdTmp = 0L;
//        Member member = currentMember.getMember();
//        QMember qMember = QMember.member;
//        QGroup qGroup = QGroup.group;
//        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
//        QFunding qFunding = QFunding.funding;
//
//        List<Tuple> results = query.select(
//                        qGroup.groupId,
//                        qGroup.groupLeader.memberId,
//                        qMember.memberName.as("groupLeaderName"),
//                        qGroup.groupName,
//                        qGroupStatus.group.countDistinct().as("groupMemberCount"),
//                        qFunding
//                )
//                .from(qGroup)
//                .join(qGroupStatus).on(qGroup.groupId.eq(qGroupStatus.group.groupId))
//                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
//                .leftJoin(qFunding).on(qFunding.group.groupId.eq(qGroup.groupId))
//                .where(qGroupStatus.groupMember.memberId.eq(member.getMemberId()))
//                .groupBy(qGroup.groupId, qGroup.groupLeader.memberId, qMember.memberName, qGroup.groupName) // 필요한 필드들을 그룹화
//                .fetch();
//        //그룹 객체
//        //그룹상태 조인
//        //그룹 리더의 멤버 객체 조인
//        //그룹펀딩이 있다면 펀딩객체 조인
//        //
//
//        Map<Long, GroupDto> groupDtoMap = new HashMap<>();
//
//        for (Tuple tuple : results) {
//            groupIdTmp = tuple.get(qGroup.groupId);
//
//            GroupDto groupDto = groupDtoMap.get(groupIdTmp);
//            if (groupDto == null) {
//                groupDto = new GroupDto();
//                groupDto.setGroupId(groupIdTmp);
//                groupDto.setGroupLeaderId(tuple.get(qGroup.groupLeader.memberId));
//                groupDto.setGroupLeaderName(tuple.get(qMember.memberName.as("groupLeaderName")));
//                groupDto.setGroupName(tuple.get(qGroup.groupName));
//                groupDto.setGroupMemberCount(tuple.get(qGroupStatus.group.countDistinct().as("groupMemberCount")));
//
//                groupDto.setGroupFunding(new ArrayList<>());
//                groupDtoMap.put(groupIdTmp, groupDto);
//            }
//
//            Funding funding = tuple.get(qFunding);
//            if (funding != null) {
//                groupDto.getGroupFunding().add(funding);
//            }
//        }
//
//        GroupDto groupDto = groupDtoMap.isEmpty() ? null : groupDtoMap.values().iterator().next();
//        System.out.println(groupDto);
//
//
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("내가 속하거나 그룹장이 아닌 그룹 목록")
//    void allGroupExceptMy() {
//        // given
//        Member member = currentMember.getMember();
//        QMember qMember = QMember.member;
//        QGroup qGroup = QGroup.group;
//        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
//        // when
//
//        List<GroupDto> groups = query.select(Projections.constructor(GroupDto.class,
//                        qGroup.groupId,
//                        qGroup.groupLeader.memberId,
//                        qMember.memberName.as("groupLeaderName"),
//                        qGroup.groupName,
//                        JPAExpressions.select(qGroupStatus.countDistinct())
//                                .from(qGroupStatus)
//                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId))
//                ))
//                .from(qGroup)
//                .join(qMember).on(qGroup.groupLeader.memberId.eq(qMember.memberId))
//                .where(
//                        qGroup.groupLeader.memberId.ne(member.getMemberId()) // 내가 그룹 리더가 아닌 그룹
//                                .and(
//                                        JPAExpressions.select(qGroupStatus.groupMember.memberId)
//                                                .from(qGroupStatus)
//                                                .where(qGroupStatus.group.groupId.eq(qGroup.groupId)
//                                                        .and(qGroupStatus.groupMember.memberId.eq(member.getMemberId())))
//                                                .notExists() // 내가 그룹에 속하지 않은 그룹
//                                )
//                )
//                .fetch();
//
//        // then
//        for (GroupDto group : groups) {
//            System.out.println(group.getGroupId());
//        }
//    }
//}
