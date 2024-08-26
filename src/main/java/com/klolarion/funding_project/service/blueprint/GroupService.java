package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.GroupStatus;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.group.GroupDto;
import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public interface GroupService {

    List<Member> invitedMembersToMyGroup(Long groupId);

    List<Member> requestedMembersToMyGroup(Long groupId);

    /*내가 그룹장인 그룹 목록 */
    List<GroupDto> myLeaderGroups();
    /*내가 참여중인 그룹 목록*/
    List<GroupDto> myGroups();

    List<GroupDto> allGroupExceptMy();

    List<GroupDto> allGroups();

    /*그룹 멤버 조회*/
    List<Member> groupMembers(Long groupId);

    /*그룹 정보 조회 */
    GroupDto groupDetail(Long groupId);

    GroupDto groupInfoDetail(Long groupId);

    /*그룹 생성*/
    Group startGroup(String groupName, String groupCategory);
    /*내 그룹 목록 */
    GroupStatus inviteMember(Long groupId, Long memberId);
    /*그룹 초대 수락 */
    boolean acceptInviteRequest(Long groupId, Long memberId);

    GroupStatus requestToGroup(Long groupId);

    boolean acceptMemberRequest(Long groupId, Long memberId);

    boolean exileMember(Long groupId, Long memberId);

    boolean exitGroup(Long groupId, Long memberId);

    boolean banMember(Long groupId, Long memberId);

    boolean groupNameCheck(String groupName);
}
