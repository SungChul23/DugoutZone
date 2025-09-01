package com.kim.SpringStudy.dugout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class PitcherSearchDTO {

    //기본정보
    private String nameKr;
    private String team;

    // ===== 기본 기록 (Basic1) =====
    private BigDecimal era;
    private Integer g;
    private Integer w;
    private Integer l;
    private Integer sv;
    private Integer hld;
    private BigDecimal wpct;
    private String ip;
    private Integer h;
    private Integer hr;
    private Integer bb;
    private Integer hbp;
    private Integer so;
    private Integer r;
    private Integer er;
    private BigDecimal whip;

    // ===== 세부 기록 (Basic2) =====
    private Integer cg;
    private Integer sho;
    private Integer qs;
    private Integer bsv;
    private Integer tbf;
    private Integer np;
    private BigDecimal avg;
    private Integer twoB;
    private Integer threeB;
    private Integer sac;
    private Integer sf;
    private Integer ibb;
    private Integer wp;
    private Integer bk;

}
