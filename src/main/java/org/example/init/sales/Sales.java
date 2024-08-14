package org.example.init.sales;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.init.member.Member;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ItemName;
    private Integer price;
    private Integer count;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="member_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Member member;
    @CreationTimestamp
    private LocalDateTime created;
}