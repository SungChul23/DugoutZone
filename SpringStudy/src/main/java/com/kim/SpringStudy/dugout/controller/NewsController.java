package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.NewsDTO;
import com.kim.SpringStudy.dugout.repository.KBOTeamRepository;
import com.kim.SpringStudy.dugout.service.TeamNewsService;
import com.kim.SpringStudy.dugout.util.TeamNameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
//각 팀 뉴스에 관한 컨트롤러
public class NewsController {

    private final KBOTeamRepository kboTeamRepository;
    private final TeamNewsService teamNewsService;


    //HTML View 페이지를 보여주는 컨트롤러
    @GetMapping("/view/{team}")
    public String viewTeamNews(@PathVariable String team, Model model) {
        String teamFullName;
        String viewFolder = TeamNameMapper.toViewFolder(team);
        if ("KBO".equalsIgnoreCase(team)) {
            teamFullName = "KBO 리그"; // 기본 페이지 풀네임 지정 NULL 방지
        } else {
            teamFullName = kboTeamRepository.findNameByCss(viewFolder)
                    .orElse(team); // 못 찾으면 team 코드 그대로
        }
        model.addAttribute("team", team); // Thymeleaf에서 JS로 넘겨줄 값
        model.addAttribute("teamFullName", teamFullName); //각 구단 한글 풀네임
        return "games/newsView";
    }

    //구단 별 뉴스
    @GetMapping("/{team}")
    @ResponseBody
    public List<NewsDTO> getTeamNews(@PathVariable String team,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int display) {

        String query = switch (team.toUpperCase()) {
            case "SSG" -> "SSG 랜더스";
            case "LG" -> "LG 트윈스";
            case "DOOSAN" -> "두산 베어스";
            case "LOTTE" -> "롯데 자이언츠";
            case "NC" -> "NC 다이노스";
            case "KIA" -> "KIA 타이거즈";
            case "KIWOOM" -> "키움 히어로즈";
            case "HANWHA" -> "한화 이글스";
            case "KT" -> "KT 위즈";
            case "SAMSUNG" -> "삼성 라이온즈";
            default -> "KBO";
        };

        return teamNewsService.getNewsByTeam(query, page, display);
    }
}
