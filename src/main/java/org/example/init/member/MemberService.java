package org.example.init.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public void saveItem(Member member){
        member.setUsername(member.getUsername());
        var hash = encoder.encode(member.getPassword());
        member.setPassword(hash);
        member.setDisplayName(member.getDisplayName());
        memberRepository.save(member);
    }
}
