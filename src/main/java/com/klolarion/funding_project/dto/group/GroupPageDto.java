package com.klolarion.funding_project.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPageDto {
    private List<GroupDto> createdGroupDtoList;
    private List<GroupDto> assignedGroupDtoList;

}
