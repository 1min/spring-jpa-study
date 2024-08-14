package org.example.init.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secretkey}")
    private String secretkey;

    // jwt 비밀키인데 @Value를 이용해 application.properties에 보관추천
    // 페이로드(claim들) + 이 SecretKey로 hmac 알고리즘으로 서명(해싱) 생성함
    // 나중에 유저로부터 jwt를 받았을 때 페이로드 부분을 이 SecretKey로 hmac 알고리즘으로
    // 해싱했을 때 유저한테 받은 jwt 안의 서명과 일치하면 위조안된거 검증가능
    private static SecretKey key;

    // @PostConstruct 붙이면 Bean생성후 @Value 같은 의존성들 주입된 후에 실행됨
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretkey));
    }

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {

        CustomUser user = (CustomUser) auth.getPrincipal();
        String authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("displayName", user.getDisplayName())
                .claim("authorities", authorities) //추가함
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000)) //유효기간 100초
                .signWith(key)
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

}