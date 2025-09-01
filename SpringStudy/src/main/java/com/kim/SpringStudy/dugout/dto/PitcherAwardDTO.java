package com.kim.SpringStudy.dugout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class PitcherAwardDTO {
    private int rank; //순위
    private String nameKr; //이름
    private String team; // 팀명
    private String positionSub; //포지션
    //int , bigdecimal값 모두 가질 수 있게 (타율과 홈런은 다른 비율임)
    private Object value; // 값
}
