package org.example.init.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class CustomUser extends User {
    public Integer id;
    public String displayName;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities,Integer id, String displayName) {
        super(username, password, authorities);
        this.id = id;
        this.displayName = displayName;
    }
}