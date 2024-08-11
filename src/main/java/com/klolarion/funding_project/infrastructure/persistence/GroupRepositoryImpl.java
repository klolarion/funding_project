package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.interfaces.dto.GroupDto;
import com.klolarion.funding_project.domain.entity.Group;
import com.klolarion.funding_project.application.port.out.GroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    @Override
    public List<GroupDto> myGroups() {
        return null;
    }

    @Override
    public GroupDto groupDetail(Long groupId) {
        return null;
    }

    @Override
    public Group startGroup(Group group) {
        return null;
    }

    @Override
    public boolean inviteMember(Long memberId) {
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
    public boolean acceptMemberRequest(Long groupStatusId) {
        return false;
    }

    @Override
    public boolean exileMember(Long memberId, Long groupId) {
        return false;
    }

    @Override
    public boolean exitGroup(Long memberId, Long groupId) {
        return false;
    }

    @Override
    public boolean banMember(Long memberId, Long groupId) {
        return false;
    }

}
