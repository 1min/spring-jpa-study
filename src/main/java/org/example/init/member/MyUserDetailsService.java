package org.example.init.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 username을 가진 유저를 찾아와서
        // return new User(유저아이디, 비번, 권한) 해주세요
        Optional<Member> result = memberRepository.findByUsername(username);
        Member user;
        if(result.isEmpty()){
            throw new UsernameNotFoundException(username);
        }

        user = result.get();
        List<GrantedAuthority> 권한목록 = new ArrayList<>();
        권한목록.add(new SimpleGrantedAuthority("일반유저"));

        return new CustomUser(user.getUsername(), user.getPassword(), 권한목록, user.getId(), user.getDisplayName());
    }
}
