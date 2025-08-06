package com.kim.SpringStudy.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyWeatherDTO {

    private int day;
    private String date; // ex 8월 5일
    private String weatherAm; //오전
    private String weatherPm; //오후
    private Integer maxTemp; //최대 기온
    private Integer minTemp; //최소 기온

    private Integer rainProbAm; //오전 강수
    private Integer rainProbPm; // 오후 강수

}
