package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public interface GroupService {
    List<Group> myGroups();
    Group groupDetail();
    Group startGroup();
    boolean inviteMember();
    boolean acceptInviteRequest();
    boolean requestToGroup();
    boolean acceptMemberRequest();
    boolean exileMember();
    boolean exitGroup();
    boolean banMember();
}
