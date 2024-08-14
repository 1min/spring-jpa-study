package org.example.init.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.init.sales.Sales;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(unique = true)
    public String username;
    public String displayName;
    public String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Sales> sales = new ArrayList<>();
}