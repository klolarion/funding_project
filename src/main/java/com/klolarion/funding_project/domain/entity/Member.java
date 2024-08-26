package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @OneToMany(mappedBy = "member")
    private List<PaymentMethodList> paymentMethodList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String account;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String email;

    @Column(nullable = false, unique = true)
    @Size(max = 100)
    private String memberName;

    private String provider; //공급자 (google, facebook ...)
    private String providerId; //공급 아이디 ?

    private boolean enabled;
    private boolean banned;
    private boolean offCd;

    public Member(Role role, String email, String memberName, String provider, String providerId) {
        this.paymentMethodList = null;
        this.role = role;
        this.email = email;
        this.memberName = memberName;
        this.provider = provider;
        this.providerId = providerId;
        this.enabled = true;
        this.banned = false;
        this.offCd = false;
    }

    public Member(String account, String name, String email, Role role) {
        this.account = account;
        this.memberName = name;
        this.email = email;
        this.role = role;
    }

}
