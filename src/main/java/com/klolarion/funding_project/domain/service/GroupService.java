package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.interfaces.dto.GroupDto;
import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> myGroups();
    GroupDto groupDetail();
    Group startGroup();
    boolean inviteMember();
    boolean acceptInviteRequest();
    boolean requestToGroup();
    boolean acceptMemberRequest();
    boolean exileMember();
    boolean exitGroup();
    boolean banMember();
}
