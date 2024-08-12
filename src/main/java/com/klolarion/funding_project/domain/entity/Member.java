package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime{
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

    @Column(nullable = false)
    private String password;

    private boolean enabled;
    private boolean offCd;

    public Member( Role role, String email, String memberName, String password) {
        this.paymentMethodList = null;
        this.role = role;
        this.email = email;
        this.memberName = memberName;
        this.password = password;
        this.enabled = false;
        this.offCd = false;
    }

    //userDetails 로 변환
    public CustomUserDetails memberToCustom() {
        return new CustomUserDetails(
                account,
                password,
                enabled,
                role
        );
    }
}
