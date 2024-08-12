package com.klolarion.funding_project.service;

import com.klolarion.funding_project.dto.GroupDto;
import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> myGroups();
    GroupDto groupDetail(Long groupId);
    Group startGroup();
    boolean inviteMember(Long groupId, Long memberId);
    boolean acceptInviteRequest(Long groupId, Long memberId);
    boolean requestToGroup(Long groupId);
    boolean acceptMemberRequest(Long groupId, Long memberId);
    boolean exileMember(Long groupId, Long memberId);
    boolean exitGroup(Long groupId, Long memberId);
    boolean banMember(Long groupId, Long memberId);
}
