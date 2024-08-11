package com.klolarion.funding_project.interfaces.dto;

import com.klolarion.funding_project.domain.entity.Funding;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupDto {
    private Long groupId;
    private Long groupLeaderId;
    private String groupLeaderName;
    private String groupName;
    private int groupMemberCount;
    private List<Funding> groupFunding;
}
