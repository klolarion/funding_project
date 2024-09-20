package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.QMember;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.exception.auth.CheckAccountException;
import com.klolarion.funding_project.exception.auth.LookAccountException;
import com.klolarion.funding_project.exception.auth.LookTelException;
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
            if(provider == null){
                throw new CheckAccountException("등록된 소셜 로그인 정보가 없습니다.");
            }

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
    public void lookAccount(String account){
        QMember qMember = QMember.member;

        Integer count = query
                .selectOne() //단일행을 선택함
                .from(qMember)
                .where(qMember.account.eq(account))
                .fetchFirst();  // 첫 번째 결과를 가져옴, 없으면 null 반환

        if (count != null) {
            throw new LookAccountException("이미 사용 중인 전화번호입니다.");
        }
    }

    @Override
    public void lookTel(String tel){
        QMember qMember = QMember.member;

        Integer count = query
                .selectOne() //단일행을 선택함
                .from(qMember)
                .where(qMember.tel.eq(tel))
                .fetchFirst();  // 첫 번째 결과를 가져옴, 없으면 null 반환

        if (count != null) {
            throw new LookTelException("이미 사용 중인 전화번호입니다.");
        }
    }
}
