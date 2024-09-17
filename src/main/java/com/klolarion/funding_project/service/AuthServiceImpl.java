package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.QMember;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.RoleRepository;
import com.klolarion.funding_project.service.blueprint.AuthService;
import com.klolarion.funding_project.util.RedisService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JPAQueryFactory query;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;


    public String checkAccount(String account){
        //캐시 provider확인
        String data = redisService.getData(account);
        if(data == null) {
            QMember qMember = QMember.member;
            //없으면 db조회 후 캐싱
            String provider = query.select(qMember.provider).from(qMember).where(qMember.account.eq(account)).fetchOne();
//            redisService.setData(account, provider, 10, TimeUnit.DAYS);
            return provider;
        }
        return data;
    };

    @Override
    public boolean register(RegisterDto registerDto) {
        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));
        Member member = new Member();
        member.register(
                registerDto.getAccount(),
                registerDto.getTel(),
                defauleRole
        );
        try {
            memberRepository.save(member);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String findAccount(String tel){
        QMember qMember = QMember.member;
        return query.select(qMember.account).from(qMember).where(qMember.tel.eq(tel)).fetchOne();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean lookAccount(String account){
        QMember qMember = QMember.member;

        Integer count = query
                .selectOne() //단일행을 선택함
                .from(qMember)
                .where(qMember.account.eq(account))
                .fetchFirst();  // 첫 번째 결과를 가져옴, 없으면 null 반환

        // count가 null이 아니면 true, 아니면 false 반환
        return count != null;
    }

    @Override
    public boolean lookTel(String tel){
        QMember qMember = QMember.member;

        Integer count = query
                .selectOne() //단일행을 선택함
                .from(qMember)
                .where(qMember.tel.eq(tel))
                .fetchFirst();  // 첫 번째 결과를 가져옴, 없으면 null 반환

        // count가 null이 아니면 true, 아니면 false 반환
        return count != null;
    }
}
