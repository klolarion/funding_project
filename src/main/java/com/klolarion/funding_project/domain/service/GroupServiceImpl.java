package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Group;

import java.util.List;

public class GroupServiceImpl implements GroupService{
    @Override
    public List<Group> myGroups() {
        return null;
    }

    @Override
    public Group groupDetail() {
        return null;
    }

    @Override
    public Group startGroup() {
        return null;
    }

    @Override
    public boolean inviteMember() {
        return false;
    }

    @Override
    public boolean acceptInviteRequest() {
        return false;
    }

    @Override
    public boolean requestToGroup() {
        return false;
    }

    @Override
    public boolean acceptMemberRequest() {
        return false;
    }

    @Override
    public boolean exileMember() {
        return false;
    }

    @Override
    public boolean exitGroup() {
        return false;
    }

    @Override
    public boolean banMember() {
        return false;
    }
}
