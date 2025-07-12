package com.kim.SpringStudy.sales;


import com.kim.SpringStudy.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String itemName; //상품명
    Integer price; // 가격 -> 이미 item 테이블에 있음
    Integer count; // 수량
    //String username; // 구매자 id -> 이미 유저테이블에 있음
    //String displayName; //구매자 실명 -> 이미 유저테이블에 있음
    @ManyToOne
            @JoinColumn(
                    name = "memberId",
                    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
            )
    private User user; //User 관계를 만들고, FK로 User를 참조

    //Long memberId; //-> username, displayName을 하나의 칼럼으로 가져옴

    @CreationTimestamp
    LocalDateTime created;


}
