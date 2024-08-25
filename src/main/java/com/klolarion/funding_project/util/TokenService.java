package com.klolarion.funding_project.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final SignKeyService signKeyService;

    @Value("${application.security.jwt.private-key}")
    private String privateKey; // for access token


    /**
     * 토큰 검증 메서드 (필터에서 호출)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String username = extractClaim(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token) &&
                isIssuerValid(token);
    }

    /**
     * 토큰 만료 여부 확인
     */
    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * 토큰 발행인 확인
     */
    private boolean isIssuerValid(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return "https://almagest-auth.com".equals(extractClaim(token, Claims::getIssuer));
    }

    /**
     * 토큰에서 모든 클레임 추출
     */
    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰에서 특정 클레임 추출
     */
    public String extractClaim(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaim(token, Claims::getSubject);
    }

    // 특정 클레임을 추출하는 일반적인 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 서명용 개인 키 가져오기
     */
    private Key getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return signKeyService.toPrivateKey(privateKey);
    }
}
