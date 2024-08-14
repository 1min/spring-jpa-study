package org.example.init.member;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // response를 이용해 리다이렉트도 가능
//        response.sendRedirect("/");


        // jwt 이름의 쿠키가 있으면 꺼내서
        Cookie[] cookies = request.getCookies();
        
        // 로그인 한적없으면 쿠키가 없을 수도 있음, 0개이면 아예 null임
        if (cookies == null){
            filterChain.doFilter(request, response); // 다음필터 실행해주세요, 책임 연쇄패턴이라 그럼
            return;
        }

        var jwtCookie = "";
        for (int i = 0; i < cookies.length; i++){
            if (cookies[i].getName().equals("jwt")){
                jwtCookie = cookies[i].getValue();
                break;
            }
        }

        // 유효기간, 위조여부등을 확인
        Claims claim;
        try {
            claim = JwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            System.out.println("유효기간 만료되거나 이상함");
            filterChain.doFilter(request, response);
            return;
        }

        // 문제없으면 auth 변수에 유저정보 넣어줘서 Controller API에서 자동주입되게
        var arr = claim.get("authorities").toString().split(",");
        var authorities = Arrays.stream(arr).map(a -> new SimpleGrantedAuthority(a)).toList();
        CustomUser customUser = new CustomUser(
                claim.get("username").toString(),
                "none",
                authorities,
                null,
                claim.get("displayName").toString()
        );


        var authToken = new UsernamePasswordAuthenticationToken(
                customUser,
                null,
                authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.getUserPrincipal();

        //요청들어올때마다 실행할코드~~
        filterChain.doFilter(request, response);
    }

}