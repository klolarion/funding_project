package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_status_id")
    private Long friendStatusId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accepter_id")
    private Member accepter;
    private boolean accepted;
    private boolean denied;

    public FriendStatus(Member requester, Member accepter, boolean accepted, boolean denied) {
        this.requester = requester;
        this.accepter = accepter;
        this.accepted = false;
        this.denied = false;
    }
}
