package com.kim.SpringStudy.dugout.controller;


import com.kim.SpringStudy.dugout.dto.WeeklyWeatherDTO;
import com.kim.SpringStudy.dugout.service.WeatherService;
import com.kim.SpringStudy.dugout.util.TeamNameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;


    //구단 별 날씨 보기
    @GetMapping
    public String showWeatherPage() {
        return "weatherInfo/weatherView";
    }

    @GetMapping("/{teamName}")
    public String showTeamWeather(@PathVariable String teamName, Model model) {
        // 한글 or 영문 → DB/API용 이름 변환
        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
        if (dbTeamName == null) {
            throw new IllegalArgumentException("지원하지 않은 팀명 :" + teamName);
        }

        System.out.println("선택한 구단 : " + teamName);

        //날씨 조회
        List<WeeklyWeatherDTO> weatherList = weatherService.getWeatherForTeam(dbTeamName);

        System.out.println(" teamName: " + teamName);
        System.out.println(" dbTeamName: " + dbTeamName);
        System.out.println(" 날씨 개수: " + weatherList.size());


        model.addAttribute("team", dbTeamName);
        model.addAttribute("weatherList", weatherList);

        return "weatherInfo/weatherDetail";
    }
}
