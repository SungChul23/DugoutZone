package com.kim.SpringStudy.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class KBOplayerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int uniformNumber;

    private String nameKr;

    private String team;      // SSG, LG, 한화 등

    private String position;  // 투수 / 포수 / 내야수 / 외야수

    private int heightCm;

    private int weightKg;

    private String birth;     // 생년월일 (예: "1988.07.22" 또는 "1988-07-22")

    private String pitchHand; // 좌투 / 우투 / null

    private String imageUrl;  // S3 이미지 URL


    //타자 , 투수에서 선수 이름을 매핑
    // 한 명의 선수가 여러 개의 타자 기록을 가질 수 있음
    //BatterStats 엔티티 안의 player 필드와 매핑
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BatterStats> batterStatsList = new ArrayList<>();
}
