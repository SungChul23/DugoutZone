package com.kim.SpringStudy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;

import java.time.LocalDate;

import lombok.Setter;
import lombok.ToString;


@Entity
@IdClass(KBOId.class)  // 추가!
@Getter
@Setter
@ToString
public class KBO {
    @Id
    private String teamName;
    @Id
    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column
    private Integer rankNum;        // 순위
    private Integer game;        // 경기 수
    private Integer win;         // 승
    private Integer lose;        // 패
    private Integer draw;        // 무
    private double winRate;  // 승률
    private double gameGap;  // 게임차
    private String recent10; // 최근 10경기
    private String streak;   // 연속 (연승/연패)
    private String home;     // 홈 경기 성적
    private String away;     // 방문 경기 성적

}
