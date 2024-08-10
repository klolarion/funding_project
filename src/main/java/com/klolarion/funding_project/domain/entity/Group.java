package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Group extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String groupName;

    private boolean accepted;
    private boolean exited;
    private boolean banned;

    public Group(Member groupLeader, Member member, String groupName) {
        this.groupLeader = groupLeader;
        this.member = member;
        this.groupName = groupName;
        this.accepted = false;
        this.exited = false;
        this.banned = false;
    }
}
