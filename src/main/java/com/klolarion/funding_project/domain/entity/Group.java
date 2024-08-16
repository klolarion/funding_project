package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_groups") //group, groups는 mysql 예약어
public class Group extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    @Column(nullable = false, unique = true, name = "group_name")
    @Size(max = 50)
    private String groupName;

    @Column(name = "group_active")
    private boolean groupActive;

    @Column(nullable = false)
    private int groupCategoryCode;


    public Group(Member groupLeader, String groupName, int code) {
        this.groupLeader = groupLeader;
        this.groupName = groupName;
        this.groupActive = true;
        this.groupCategoryCode = code;
    }
}
