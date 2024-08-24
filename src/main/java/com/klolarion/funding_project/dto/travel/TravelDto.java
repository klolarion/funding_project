package com.klolarion.funding_project.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelDto {
    private Long travelId;
    private String travelName;
    private String city;
    private Long groupId;
    private Long memberId;
    private Long fundingId;
    private String description;
    private Long price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
