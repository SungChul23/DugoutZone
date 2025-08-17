package com.kim.SpringStudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class BatterSearchDTO {

    private String nameKr;
    private String team;
    private BigDecimal avg;
    private Integer g;
    private Integer pa;
    private Integer ab;
    private Integer r;
    private Integer h;
    private Integer twoB;
    private Integer threeB;
    private Integer hr;
    private Integer tb;
    private Integer rbi;
    private Integer sac;
    private Integer sf;
    private Integer bb;
    private Integer ibb;
    private Integer hbp;
    private Integer so;
    private Integer gdp;
    private BigDecimal slg;
    private BigDecimal obp;
    private BigDecimal ops;
    private Integer mh;
    private BigDecimal risp;
    private BigDecimal phBa;
}
