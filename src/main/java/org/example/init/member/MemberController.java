package org.example.init.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/member")
    public String addMember(@ModelAttribute Member member) {
        memberService.saveItem(member);
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth) {
        System.out.println(auth);
        System.out.println(auth.getName()); //아이디출력가능
        System.out.println(auth.isAuthenticated()); //로그인여부 검사가능
        System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("일반유저")));
        CustomUser user = (CustomUser) auth.getPrincipal();
        System.out.println(user.displayName);
        return "mypage.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public String 로그인사용자(){
        System.out.println("admin");
        return "list.html";
    }

    @GetMapping("/user/1")
    @ResponseBody
    public Data user() {
        var a = memberRepository.findById(1);
        var data = new Data(a.get().username, a.get().displayName);
        return data;
    }

    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response){

        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"), data.get("password")
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        // auth와 auth2는 사실 같음
        var auth2 = SecurityContextHolder.getContext().getAuthentication();
        var jwt = JwtUtil.createToken(auth2);

        var cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(100);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return jwt;
    }

    @GetMapping("/my-page/jwt")
    @ResponseBody
    String myPageJWT(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();
        System.out.println(user);
        System.out.println(user.displayName);
        System.out.println(user.getAuthorities());
        return "success";
    }

}

@Getter
@Setter
class Data {
    private String username;
    private String displayName;
    public Data(String username, String displayName){
        this.username = username;
        this.displayName = displayName;
    }
}