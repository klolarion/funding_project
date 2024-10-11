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

    @Column(unique = true)
    @Size(max = 50)
    private String email;

    @Column(unique = true)
    @Size(max = 100)
    private String nickName;

    private String provider; //공급자 (google, facebook ...)

    private boolean enabled;
    private boolean banned;
    private boolean offCd;


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

}
