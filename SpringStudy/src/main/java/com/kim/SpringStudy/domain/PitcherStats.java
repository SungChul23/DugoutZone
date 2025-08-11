package com.kim.SpringStudy.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pitcher_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PitcherStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 선수 정보 (KBOplayerInfo 연동)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false) // 선수 PK
    private KBOplayerInfo player;

    @Column(nullable = false)
    private LocalDate recordDate;  // 기록 날짜

    // ===== 기본 기록 (Basic1) =====
    private BigDecimal era;   // 평균자책점 (Earned Run Average)
    private Integer g;        // 출장 경기수 (Games)
    private Integer w;        // 승 (Wins)
    private Integer l;        // 패 (Losses)
    private Integer sv;       // 세이브 (Saves)
    private Integer hld;      // 홀드 (Holds)
    private BigDecimal wpct;  // 승률 (Winning Percentage)
    @Column(length = 16)
    private String ip;        // 이닝 pitched (Innings Pitched) - 소수점 형태 유지
    private Integer h;        // 피안타 (Hits allowed)
    private Integer hr;       // 피홈런 (Home Runs allowed)
    private Integer bb;       // 볼넷 (Base on Balls)
    private Integer hbp;      // 사구 (Hit By Pitch)
    private Integer so;       // 삼진 (Strikeouts)
    private Integer r;        // 실점 (Runs)
    private Integer er;       // 자책점 (Earned Runs)
    private BigDecimal whip;  // 이닝당 출루허용률 (Walks+Hits per Inning Pitched)

    // ===== 세부 기록 (Basic2) =====
    private Integer cg;       // 완투 (Complete Games)
    private Integer sho;      // 완봉승 (Shutouts)
    private Integer qs;       // 퀄리티 스타트 (Quality Starts)
    private Integer bsv;      // 블론 세이브 (Blown Saves)
    private Integer tbf;      // 상대 타자 수 (Total Batters Faced)
    private Integer np;       // 투구 수 (Number of Pitches)
    private BigDecimal avg;   // 피안타율 (Batting Average Against)
    @Column(name = "two_b")
    private Integer twoB;     // 2루타 허용 (Doubles allowed)
    @Column(name = "three_b")
    private Integer threeB;   // 3루타 허용 (Triples allowed)
    private Integer sac;      // 희생번트 허용 (Sacrifice Bunts)
    private Integer sf;       // 희생플라이 허용 (Sacrifice Flies)
    private Integer ibb;      // 고의사구 (Intentional Base on Balls)
    private Integer wp;       // 폭투 (Wild Pitches)
    private Integer bk;       // 보크 (Balks)
}
