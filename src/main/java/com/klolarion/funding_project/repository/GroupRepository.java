package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
//
//    /*내 그룹 목록*/
//    List<GroupDto> myGroups();
//
//    /*그룹 정보*/
//    GroupDto groupDetail(Long groupId);
//
//    /*그룹 생성*/
//    Group startGroup(Group group);
//
//    /*그룹원 초대*/
//    boolean inviteMember(Long memberId);
//
//    /*그룹초대 수락*/
//    boolean acceptInviteRequest(Long groupStatusId, Long memberId);
//
//    /*그룹 가입신청*/
//    boolean requestToGroup(Long groupStatusId);
//
//    /*가입신청 수락*/
//    boolean acceptMemberRequest(Long groupStatusId);
//
//    /*그룹원 추방*/
//    boolean exileMember(Long memberId, Long groupId);
//
//    /*그룹 나가기*/
//    boolean exitGroup(Long memberId, Long groupId);
//
//    /*차단*/
//    boolean banMember(Long memberId, Long groupId);
}
