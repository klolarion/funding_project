package com.klolarion.funding_project.service;

import com.klolarion.funding_project.dto.GroupDto;
import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public interface GroupService {

    /*내 그룹 목록 */
    List<GroupDto> myGroups();
    /*그룹 정보 조회 */
    GroupDto groupDetail(Long groupId);
    /*그룹 생성*/
    Group startGroup();
    /*내 그룹 목록 */
    boolean inviteMember(Long groupId, Long memberId);
    /*그룹 초대 수락 */
    boolean acceptInviteRequest(Long groupId, Long memberId);

    boolean requestToGroup(Long groupId);

    boolean acceptMemberRequest(Long groupId, Long memberId);

    boolean exileMember(Long groupId, Long memberId);

    boolean exitGroup(Long groupId, Long memberId);

    boolean banMember(Long groupId, Long memberId);
}
