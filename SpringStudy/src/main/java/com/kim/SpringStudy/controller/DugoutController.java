package com.kim.SpringStudy.controller;


import com.kim.SpringStudy.domain.KBO;
import com.kim.SpringStudy.domain.KBOTeam;
import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.dto.GameDateDTO;
import com.kim.SpringStudy.dto.WeeklyWeatherDTO;
import com.kim.SpringStudy.repository.KBORepository;
import com.kim.SpringStudy.repository.KBOTeamRepository;
import com.kim.SpringStudy.repository.KBOplayerInfoRepository;
import com.kim.SpringStudy.service.GameDateService;
import com.kim.SpringStudy.service.KBOService;
import com.kim.SpringStudy.service.TeamNewsService;
import com.kim.SpringStudy.dto.NewsDTO;
import com.kim.SpringStudy.service.WeatherService;
import com.kim.SpringStudy.util.TeamNameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.kim.SpringStudy.util.TeamLogoMapper.teamLogoMap;

@Controller
@RequiredArgsConstructor
public class DugoutController {

    private final KBOService kboService;
    private final KBOTeamRepository kboTeamRepository;
    private final TeamNewsService teamNewsService;
    private final KBORepository kboRepository;
    private final KBOplayerInfoRepository kbOplayerInfoRepository;
    private final GameDateService gameDateService;
    private final WeatherService weatherService;



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
    //구단 별 뉴스
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
        String viewFolder = TeamNameMapper.toViewFolder(teamName);
        if (viewFolder == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (viewFolder): " + teamName);
        }

        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
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

    // 구단별 선수진
    @GetMapping("/team/{teamName}/player")
    public String player(@PathVariable String teamName,
                         @RequestParam(defaultValue = "투수") String position,
                         @RequestParam(required = false) String keyword,
                         Model model) {

        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
        if (dbTeamName == null) {
            throw new IllegalArgumentException("DB 팀명 매핑 실패: " + teamName);
        }

        List<KBOplayerInfo> result;

        if (keyword != null && !keyword.isBlank()) {
            result = kbOplayerInfoRepository.findByTeamAndNameKrContaining(dbTeamName, keyword);
        } else {
            result = kbOplayerInfoRepository.findByTeamAndPosition(dbTeamName, position);
        }

        model.addAttribute("players", result);
        model.addAttribute("teamName", teamName); // ← URL용 원본
        model.addAttribute("position", position);
        model.addAttribute("keyword", keyword);

        return "teams/playerView";
    }
    // KBO 2025시즌 일정
    @GetMapping("/gamedate")
    public String test(@RequestParam(required = false) String date, Model model) {
        // 모든 날짜 리스트 가져옴
        List<String> availableDates = gameDateService.getAvailableDates();

        // 오늘 날짜 계산 ("08.02" 같은 형식으로)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM.dd"));

        // 오늘 날짜가 DB에 있으면 그것을 사용
        String selectedDate = (date != null) ? date :
                (availableDates.contains(today) ? today :
                        gameDateService.getLatestAvailableDate());

        System.out.println("선택된 날짜 : " + selectedDate);

        // 해당 날짜의 경기 목록 조회
        List<GameDateDTO> games = gameDateService.getGamesByDate(selectedDate);

        // 모델에 전달
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("games", games);

        model.addAttribute("teamLogoMap", teamLogoMap);
        return "scheduleView";
    }
    //구단 별 날씨 보기
    @GetMapping("/weather")
    public String showWeatherPage(){
        return "weatherView";
    }
    @GetMapping("/weather/{teamName}")
    public String showTeamWeather(@PathVariable String teamName, Model model){
        // 한글 or 영문 → DB/API용 이름 변환
        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
        if(dbTeamName == null){
            throw  new IllegalArgumentException("지원하지 않은 팀명 :" + teamName);
        }

        System.out.println("선택한 구단 : " + teamName);

        //날씨 조회
        List<WeeklyWeatherDTO> weatherList = weatherService.getWeatherForTeam(dbTeamName);

        System.out.println("✅ teamName: " + teamName);
        System.out.println("✅ dbTeamName: " + dbTeamName);
        System.out.println("✅ 날씨 개수: " + weatherList.size());


        model.addAttribute("team", dbTeamName);
        model.addAttribute("weatherList", weatherList);

        return "weatherDetail";
    }


}
