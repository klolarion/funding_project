//package com.klolarion.funding_project.service;
//
//import com.klolarion.funding_project.domain.entity.CsrfToken;
//import com.klolarion.funding_project.util.RedisService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Function;
//
//@Service
//@RequiredArgsConstructor
//public class TokenService {
//    private final RedisService redisService;
//
//    @Value("${application.security.jwt.private-key}")
//    private String privateKey;
//
//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
//
//    private long TOKEN_EXPIRE_TIME = 1036800000;
//
//    private String publicKey = "";
//
//    /**
//     * 계정정보와 claim추출, 토큰검증에서 사용
//     * extractAccount()를 호출하면 extractClaim()과 extractAllClaims()를 연달아 호출한다.
//     * */
//    //토큰에서 유저계정정보를 추출한다.
//    public String extractAccount(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractClaim(token, Claims::getSubject);
//    }
//    //토큰에서 만료일자를 추출한다.
//    private Date extractExpiration(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractClaim(token, Claims::getExpiration);
//    }
//    //토큰 발행인 추출
//    private String extractIssuer(String token) throws NoSuchAlgorithmException, InvalidKeySpecException { return extractClaim(token, Claims::getIssuer); }
//    //토큰에서 claim을 추출한다.
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String extractCsrfClaim(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        Claims claims = extractAllClaims(token);
//        return claims.get("cft").toString();
//    }
//
//
//    //개인키로 토큰조회
//    //claim이란 사용자에 대한 property를 의미한다. id, role 등
//    //jwtParser로 토큰에서 정보를 읽어온다.
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(privateKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//
//    /**
//     * 토큰검증, 토큰필터에서 사용
//     * isTokenValid()를 호출하면 isTokenExpired()와 extractExpiration()을 연달아 호출한다.
//     * */
//    //jwt검증
//    public boolean isTokenValid(String token, UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        final String account = extractAccount(token); //토큰에서 계정명을 추출
//        //토큰검증여부리턴
//        return (account.equals(userDetails.getUsername())) && //계정명 매칭 확인
//                !isTokenExpired(token) && //만료여부 확인
//                issuerMatches(token); //발급인 일치여부 확인
//    }
//
//    //csrf토큰 검증
//    public boolean isCsrfTokenValid(String csrfToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        final String account = extractAccount(csrfToken); //토큰에서 계정명을 추출
//        final String csrfTokenBody = extractCsrfClaim(csrfToken);
//        return csrfMatches(csrfTokenBody, account) && issuerMatches(csrfToken);
//    }
//
//    //토큰만료여부검증
//    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractExpiration(token).before(new Date());
//    }
//
//    //토큰 발행인 검증
//    private boolean issuerMatches(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return extractIssuer(token).equals("https://almagest-auth.com");
//    }
//
//    private boolean csrfMatches(String tokenBody, String account){
//        String csrfToken = redisService.getData(account);
//        return csrfToken.equals(tokenBody);
//    }
//
//
//    /**
//     * 토큰생성
//     * userDeatils를 받아 서명된 토큰을 생성하고 리턴한다.
//     * */
//    //userDetails로 토큰을 생성한다.
//    public String generateCsrfToken(UserDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return generateCsrfToken(new HashMap<>(), userDetails);
//    }
//
//
//    public String generateCsrfToken(
//            Map<String, Object> csrfClaims,
//            UserDetails userDetails
//    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        CsrfToken csrfToken = new CsrfToken(userDetails.getUsername(), csrfClaims.get("cft").toString());
//        try {
//            redisService.setData(csrfToken.getAccount(), csrfToken.getToken(), TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
//        }catch (Exception e){
//            return null;
//        }
//        return buildCsrfToken(csrfClaims, userDetails, jwtExpiration);
//    }
//
//    //csrf토큰 빌더
//    private String buildCsrfToken(
//            Map<String, Object> csrfClaims,
//            UserDetails userDetails,
//            long expiration
//    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        return Jwts
//                .builder()
//                .setClaims(csrfClaims) //csrf토큰 로드
//                .setIssuer("https://almagest-auth.com")//발행주체
//                .setSubject(userDetails.getUsername())//username 설정. userDetails에서 가져온다.
//                .setIssuedAt(new Date(System.currentTimeMillis()))//현재시간
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))//만료시간 밀리초 * 초 * 분 * 시(1일)
//                .signWith(SignatureAlgorithm.RS256, publicKey)//서명정보.
//                .compact();
//    }
//
//
//}
