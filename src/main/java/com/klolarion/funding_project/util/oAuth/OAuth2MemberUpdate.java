package com.klolarion.funding_project.util.oAuth;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2MemberUpdate {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    public Member saveOrUpdateUserGoogle(OAuth2User oAuth2User) {

        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        boolean enabled = oAuth2User.getAttribute("email_verified");

        return memberRepository.findByAccount(googleId)
                .map(member -> {
                    member.setEmail(email);
                    member.setMemberName(name);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Member member = new Member();
                    member.setAccount(googleId);
                    member.setEmail(email);
                    member.setMemberName(name);
                    member.setRole(defauleRole); // 기본 권한 설정
                    member.setProvider("Google");
                    member.setTel("");
                    member.setEnabled(enabled);
                    return memberRepository.save(member);
                });
    }


    public Member saveOrUpdateUserNaver(OAuth2User oAuth2User) {
        System.out.printf("hi");
        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));
//        System.out.println("oauth" + oAuth2User);
        String naverId = oAuth2User.getAttribute("id");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
//        boolean enabled = oAuth2User.getAttribute("email_verified");

        return memberRepository.findByAccount(naverId)
                .map(member -> {
                    member.setEmail(email);
                    member.setMemberName(name);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Member member = new Member();
                    member.setAccount(naverId);
                    member.setEmail(email);
                    member.setMemberName(name);
                    member.setRole(defauleRole); // 기본 권한 설정
                    member.setProvider("Naver");
                    member.setTel(" ");
//                    member.setEnabled(enabled);
                    return memberRepository.save(member);
                });
//        return null;
    }


    public Member saveOrUpdateUserKakao(OAuth2User oAuth2User) {
        System.out.printf("hi");
        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));
//        System.out.println("oauth" + oAuth2User);
        String kakaoId = oAuth2User.getAttribute("id");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
//        boolean enabled = oAuth2User.getAttribute("email_verified");

        return memberRepository.findByAccount(kakaoId)
                .map(member -> {
                    member.setEmail(email);
                    member.setMemberName(name);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Member member = new Member();
                    member.setAccount(kakaoId);
                    member.setEmail(email);
                    member.setMemberName(name);
                    member.setRole(defauleRole); // 기본 권한 설정
                    member.setProvider("Kakao");
                    member.setTel(" ");
//                    member.setEnabled(enabled);
                    return memberRepository.save(member);
                });
//        return null;
    }

}
