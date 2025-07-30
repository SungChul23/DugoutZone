package com.kim.SpringStudy.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

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
}
