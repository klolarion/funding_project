package com.klolarion.funding_project.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupDto {
    private String groupName;
    private String groupCategory;
}
