package com.klolarion.funding_project.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFriendDto {
    private Long memberId;
    private String memberName;
}
