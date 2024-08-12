package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long friendId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accepter_id")
    private Member accepter;


    private boolean deleted;
    private boolean banned;

    public Friend(Member requester, Member accepter) {
        this.requester = requester;
        this.accepter = accepter;
        this.deleted = false;
        this.banned = false;
    }
}
