package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatus extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberStatusId;


    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;


    private int memberStatusCode;
    private LocalDateTime statusExpires;
    private boolean closed;

    public MemberStatus(int memberStatusCode, LocalDateTime statusExpires) {
        this.memberStatusCode = memberStatusCode;
        this.statusExpires = statusExpires;
        this.closed = false;
    }
}
