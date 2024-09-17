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

    @Column(unique = true)
    @Size(max = 50)
    private String email;

    @Column(nullable = false, unique = true)
    @Size(max = 20)
    private String tel;

    @Column(unique = true)
    @Size(max = 100)
    private String memberName;

    private String provider; //공급자 (google, facebook ...)
    private String providerId; //공급 아이디 ?

    private boolean enabled;
    private boolean banned;
    private boolean offCd;


    /*최초 사용자 등록*/
    public void register(String account, String tel, Role role){
        this.account = account;
        this.tel = tel;
        this.role = role;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void disableMember(){
        this.enabled = false;
    }

    public void banMember(){
        this.banned = true;
    }

    public void setMemberResigned(){
        this.offCd = true;
    }

    public void changeTel(String tel){
        this.tel = tel;
    }

    public void setProvider(String provider, String providerId){
        this.provider = provider;
        this.providerId = providerId;
    }
}
