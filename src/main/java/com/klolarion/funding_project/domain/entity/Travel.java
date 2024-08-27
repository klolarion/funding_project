package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자가 여행객체 생성 후
 * 펀딩 생성 진행
 * */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Travel extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long travelId;

    @Column(nullable = false)
    private String city;
    private Long groupId;
    private Long memberId;

    private String travelName;

    @OneToOne(mappedBy = "travel")
    private Funding funding;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    @Size(min = 20, max = 100)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;

    public Travel(String city, Long groupId, String travelName, Long memberId, Funding funding, Long price, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.city = city;
        this.groupId = groupId;
        this.travelName = travelName;
        this.memberId = memberId;
        this.funding = funding;
        this.price = price;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
