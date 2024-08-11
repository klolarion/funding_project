package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String groupName;

    private boolean groupActive;


    public Group(Member groupLeader, String groupName) {
        this.groupLeader = groupLeader;
        this.groupName = groupName;
        this.groupActive = true;
    }
}
