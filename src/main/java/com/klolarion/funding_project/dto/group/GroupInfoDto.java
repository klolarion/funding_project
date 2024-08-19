package com.klolarion.funding_project.dto.group;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfoDto {
    private GroupDto groupDto;
    private List<Member> members;
    private List<FundingListDto> singleList;
}
