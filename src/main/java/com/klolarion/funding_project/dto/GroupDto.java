package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.domain.entity.Funding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 그룹 정보 조회용
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private Long groupId;
    private Long groupLeaderId;
    private String groupLeaderName;
    private String groupName;
    private Long groupMemberCount;
    private List<Funding> groupFunding;
}
