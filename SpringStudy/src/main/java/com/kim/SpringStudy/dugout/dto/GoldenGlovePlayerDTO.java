package com.kim.SpringStudy.dugout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class GoldenGlovePlayerDTO {

    private String name;         // 선수 이름
    private String team;         // 소속팀
    private String position;     // 포지션 (ex. 투수, 포수, 외야수)
    private String positionSub;  // 세부 포지션 (ex. 1루수, 유격수)
    private int goldenGloveScore;     // 골든글러브 점수 (계산된 값)
}
