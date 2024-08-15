package com.klolarion.funding_project.dto.group;

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

    public GroupDto(Long groupId, Long groupLeaderId, String groupLeaderName, String groupName, Long groupMemberCount) {
        this.groupId = groupId;
        this.groupLeaderId = groupLeaderId;
        this.groupLeaderName = groupLeaderName;
        this.groupName = groupName;
        this.groupMemberCount = groupMemberCount;
    }

}
