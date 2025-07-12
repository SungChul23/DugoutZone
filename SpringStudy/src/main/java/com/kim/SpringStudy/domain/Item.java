package com.kim.SpringStudy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString
//인덱스 생성
@Table(indexes = @Index(columnList = "title" , name = "상품검색"))
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //기본키

    @Setter
    private String title;
    @Setter
    private Integer price;
    @Setter
    @Getter
    private String imageUrl;

    public void setId(Long id) {
    }
}