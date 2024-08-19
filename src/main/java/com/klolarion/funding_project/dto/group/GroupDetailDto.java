package com.klolarion.funding_project.dto.group;

import com.klolarion.funding_project.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailDto {
    private GroupDto groupDto;
    private List<Member> groupMembers;
    private List<Member> requestedMembersToMyGroup;
    private List<Member> invitedMembersToMyGroup;
}
