package com.kim.SpringStudy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(
        name = "batter_stats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id","recordDate"})
)
public class BatterStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 여러 기록이 한 명의 선수를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private KBOplayerInfo player; // 선수 기본 정보(FK)

    private LocalDate recordDate; // 해당 기록 날짜

    @Column(precision = 5, scale = 3)
    private BigDecimal avg; // 타율(AVG)

    private Integer g; // 경기수(G)
    private Integer pa; // 타석(PA)
    private Integer ab; // 타수(AB)
    private Integer r;  // 득점(R)
    private Integer h;  // 안타(H)
    private Integer twoB; // 2루타(2B)
    private Integer threeB; // 3루타(3B)
    private Integer hr; // 홈런(HR)
    private Integer tb; // 총루타(TB)
    private Integer rbi; // 타점(RBI)
    private Integer sac; // 희생번트(SAC)
    private Integer sf;  // 희생플라이(SF)
    private Integer hbp; // 사구(HBP)
    private Integer so;  // 삼진(SO)
    private Integer gdp; // 병살타(GDP)

    @Column(precision = 5, scale = 3)
    private BigDecimal slg; // 장타율(SLG)

    @Column(precision = 5, scale = 3)
    private BigDecimal obp; // 출루율(OBP)

    @Column(precision = 5, scale = 3)
    private BigDecimal ops; // OPS

    private Integer mh; // 멀티히트(MH)

    @Column(precision = 5, scale = 3)
    private BigDecimal risp; // 득점권 타율(RISP)

    @Column(precision = 5, scale = 3)
    private BigDecimal phBa; // 대타 타율(PH-BA)

    private Integer bb;  // 볼넷(BB)
    private Integer ibb; // 고의사구(IBB)

    // 정밀한 기록을 위해 BigDecimal에 scale 지정
}
