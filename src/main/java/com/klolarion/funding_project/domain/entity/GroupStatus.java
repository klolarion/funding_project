package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_status_id")
    private Long groupStatusId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_member_id")
    private Member groupMember;

    private boolean invited;
    private boolean requested;

    private boolean accepted;
    private boolean exited;
    private boolean banned;

    public GroupStatus(Group group, Member groupLeader, Member groupMember, boolean invited, boolean requested) {
        this.group = group;
        this.groupLeader = groupLeader;
        this.groupMember = groupMember;
        this.invited = invited;
        this.requested = requested;
        this.accepted = false;
        this.exited = false;
        this.banned = false;
    }
}
