package com.klolarion.funding_project.util;

import com.klolarion.funding_project.domain.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final ClaimsGenerator claimsGenerator;
    private final RedisService redisService;
    private final SignKeyService signKeyService;

//    @Value("${application.security.jwt.secret-key}")
//    private String secretKey; // for admin token
    @Value("${application.security.jwt.private-key}")
    private String privateKey; // for access token
//    @Value("${application.security.jwt.expiration}0")
//    private long jwtExpiration; // for admin token
//
//    @Value("${auth.server.url}")
//    private String authServerUrl;

    private final RestTemplate restTemplate;


    /**
     * 계정정보와 claim추출, 토큰검증에서 사용
     * extractAccount()를 호출하면 extractClaim()과 extractAllClaims()를 연달아 호출한다.
     * */
    //토큰에서 유저계정정보를 추출한다.
    public String extractAccount(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    //토큰에서 만료일자를 추출한다.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    //토큰 발행인 추출
    private String extractIssuer(String token)  { return extractClaim(token, Claims::getIssuer); }
    //토큰에서 claim을 추출한다.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractCsrfClaim(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("cft").toString();
    }

    //claim이란 사용자에 대한 property를 의미한다. id, role 등
    //jwtParser로 토큰에서 정보를 읽어온다.
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    //almagest 사용자용
    public String extractMemberAccount(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractMemberClaim(token, Claims::getSubject);
    }
    private Date extractMemberExpiration(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractMemberClaim(token, Claims::getExpiration);
    }
    private String extractMemberIssuer(String token) throws NoSuchAlgorithmException, InvalidKeySpecException { return extractMemberClaim(token, Claims::getIssuer); }
    public <T> T extractMemberClaim(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = extractAllMemberClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractMemberCsrfClaim(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Claims claims = extractAllMemberClaims(token);
        return claims.get("cft").toString();
    }
    private Claims extractAllMemberClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 토큰검증, 토큰필터에서 사용
     * isTokenValid()를 호출하면 isTokenExpired()와 extractExpiration()을 연달아 호출한다.
     * */
    //jwt검증
    public boolean isMemberTokenValid(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String account = extractMemberAccount(token); //토큰에서 계정명을 추출
        //토큰검증여부리턴
        return (account.equals(userDetails.getUsername())) && //계정명 매칭 확인
                !isMemberTokenExpired(token) && //만료여부 확인
                jwtMemberIssuerMatches(token); //발급인 일치여부 확인
    }

    public boolean isAdminTokenValid(String token, UserDetails userDetails) {
        final String account = extractAccount(token); //토큰에서 계정명을 추출
        //토큰검증여부리턴
        return (account.equals(userDetails.getUsername())) && //계정명 매칭 확인
                !isAdminTokenExpired(token) && //만료여부 확인
                jwtAdminIssuerMatches(token); //발급인 일치여부 확인
    }

    //csrf토큰 검증
    public boolean isMemberCsrfTokenValid(String csrfToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String account = extractMemberAccount(csrfToken); //토큰에서 계정명을 추출
        final String csrfTokenBody = extractMemberCsrfClaim(csrfToken);
        return memberCsrfMatches(csrfTokenBody, account) && csrfMemberIssuerMatches(csrfToken);
    }

    public boolean isAdminCsrfTokenValid(String csrfToken){
        final String account = extractAccount(csrfToken); //토큰에서 계정명을 추출
        final String csrfTokenBody = extractCsrfClaim(csrfToken);
        return adminCsrfMatches(csrfTokenBody, account) && csrfAdminIssuerMatches(csrfToken);
    }

    //토큰만료여부검증
    private boolean isAdminTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private boolean isMemberTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractMemberExpiration(token).before(new Date());
    }

    //almagest 토큰 발행인 검증
    private boolean jwtMemberIssuerMatches(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractMemberIssuer(token).equals("https://almagest-auth.com");
    }
    private boolean jwtAdminIssuerMatches(String token) {return extractIssuer(token).equals("https://funding.com");}

    //pos-csrf 토큰 발행인 검증
    private boolean csrfAdminIssuerMatches(String token) {
        return extractIssuer(token).equals("https://funding.com");
    }

    private boolean csrfMemberIssuerMatches(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractMemberIssuer(token).equals("https://almagest-auth.com");
    }

    //토큰 cft와 redis에 저장된 body 일치여부 확인
    private boolean adminCsrfMatches(String tokenBody, String account){
        Member csrfToken = (Member) redisService.getData(account+"_funding_admin");
        return csrfToken.equals(tokenBody);
    }
    private boolean memberCsrfMatches(String tokenBody, String account){
        Member csrfToken = (Member) redisService.getData(account);
        return csrfToken.equals(tokenBody);
    }



    /**
     * 보안키로 서명
     * */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return signKeyService.toPublicKey(privateKey);
    }

}
