package com.kim.SpringStudy.controller;


import com.kim.SpringStudy.domain.KBO;
import com.kim.SpringStudy.domain.KBOTeam;
import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.repository.KBORepository;
import com.kim.SpringStudy.repository.KBOTeamRepository;
import com.kim.SpringStudy.repository.KBOplayerInfoRepository;
import com.kim.SpringStudy.service.KBOService;
import com.kim.SpringStudy.service.TeamNewsService;
import com.kim.SpringStudy.dto.NewsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class DugoutController {

    private final KBOService kboService;
    private final KBOTeamRepository kboTeamRepository;
    private final TeamNewsService teamNewsService;
    private final KBORepository kboRepository;
    private final KBOplayerInfoRepository kbOplayerInfoRepository;

    //키 + 벨류 = DB + 경로

    private static final Map<String, String> TEAM_MAP = Map.ofEntries(
            Map.entry("LG", "lg"),
            Map.entry("SSG", "ssg"),
            Map.entry("KIA", "kia"),
            Map.entry("KT", "kt"),
            Map.entry("NC", "nc"),
            Map.entry("DOOSAN", "doosan"),
            Map.entry("두산", "doosan"),

            Map.entry("LOTTE", "lotte"),
            Map.entry("롯데", "lotte"),

            Map.entry("SAMSUNG", "samsung"),
            Map.entry("삼성", "samsung"),

            Map.entry("KIWOOM", "kiwoom"),
            Map.entry("키움", "kiwoom"),

            Map.entry("HANWHA", "hanwha"),
            Map.entry("한화", "hanwha")
    );


    private static final Map<String, String> DB_TEAM_NAME_MAP = Map.ofEntries(
            Map.entry("LG", "LG"),
            Map.entry("SSG", "SSG"),
            Map.entry("KIA", "KIA"),
            Map.entry("KT", "KT"),
            Map.entry("NC", "NC"),
            Map.entry("DOOSAN", "두산"),
            Map.entry("두산", "두산"),

            Map.entry("LOTTE", "롯데"),
            Map.entry("롯데", "롯데"),

            Map.entry("SAMSUNG", "삼성"),
            Map.entry("삼성", "삼성"),

            Map.entry("KIWOOM", "키움"),
            Map.entry("키움", "키움"),

            Map.entry("HANWHA", "한화"),
            Map.entry("한화", "한화")
    );


    //더그 아웃 입장
    @GetMapping("/dugout")
    public String Dugout() {
        return "dugout";
    }

    //팀별 순위 그래프
    @GetMapping("/kbo/teamRank")
    public String showTeamRank(@RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate date,
                               Model model) {
        if (date == null) {
            List<LocalDate> dates = kboService.getAvailableDates();
            if (!dates.isEmpty()) date = dates.get(0); // 최신 날짜 선택
        }

        List<KBO> teams = kboService.getTeamRankByDate(date);
        List<LocalDate> allDates = kboService.getAvailableDates();

        model.addAttribute("teams", teams);
        model.addAttribute("dates", allDates);
        model.addAttribute("selectedDate", date);

        //그래프 그리기
        List<String> teamNames = teams.stream().map(KBO::getTeamName).toList();
        List<Double> winRates = teams.stream().map(KBO::getWinRate).toList();
        model.addAttribute("teamNames", teamNames);
        model.addAttribute("winRates", winRates);

        return "teamRank";
    }
    @GetMapping("/kbo/rank-timeline")
    @ResponseBody
    public Map<String, Map<String, Integer>> getRankTimeline() {
        List<KBO> records = kboRepository.findAllByOrderByRecordDateAscTeamNameAsc();

        Map<String, Map<String, Integer>> timeline = new TreeMap<>();

        for (KBO record : records) {
            String date = record.getRecordDate().toString();
            String team = record.getTeamName();
            Integer rank = record.getRankNum();

            timeline
                    .computeIfAbsent(date, k -> new HashMap<>())
                    .put(team, rank);
        }

        return timeline;
    }


    //구단 별 예매 링크
    @GetMapping("/tickets")
    public String Tickets(Model model) {
        List<KBOTeam> result = kboTeamRepository.findAll();
        model.addAttribute("topTeams", result.subList(0, 5));
        model.addAttribute("bottomTeams", result.subList(5, 10));
        return "tickets";
    }

    //HTML View 페이지를 보여주는 컨트롤러
    @GetMapping("/news/view/{team}")
    public String viewTeamNews(@PathVariable String team, Model model) {
        model.addAttribute("team", team); // Thymeleaf에서 JS로 넘겨줄 값
        return "newsView"; // → templates/newsView.html
    }

    @GetMapping("/news/{team}")
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

    //각 팀 사이트 배너에 객체들
    @GetMapping("/team/{teamName}")
    public String teamPage(@PathVariable String teamName, Model model) {
        String key = teamName.toUpperCase(); // URL에서 받은 팀 이름 (영어 또는 한글)

        String viewFolder = TEAM_MAP.getOrDefault(key, null);
        if (viewFolder == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (viewFolder): " + teamName);
        }

        String dbTeamName = DB_TEAM_NAME_MAP.getOrDefault(key, null);
        if (dbTeamName == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (DB 조회용): " + teamName);
        }

        Optional<KBO> record = kboRepository.findLatestByTeamName(dbTeamName);
        if (record.isEmpty()) {
            throw new IllegalStateException("기록이 존재하지 않습니다: " + teamName);
        }

        model.addAttribute("record", record.get());
        return "teams/" + viewFolder;
    }


    @GetMapping("/team/{teamName}/player")
    public String player(@PathVariable String teamName,
                         @RequestParam(defaultValue = "투수") String position,
                         Model model) {

        String key = teamName.toUpperCase(); // DB에 조회할려고 어퍼케이스

        String dbTeamName = DB_TEAM_NAME_MAP.getOrDefault(key, null);

        if (dbTeamName == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (DB 조회용): " + teamName);
        }

        List<KBOplayerInfo> players = kbOplayerInfoRepository.findByTeamAndPosition(dbTeamName, position);

        model.addAttribute("teamName", teamName);
        model.addAttribute("position", position);
        model.addAttribute("players", players);
        return "teams/playerView";
    }

}
