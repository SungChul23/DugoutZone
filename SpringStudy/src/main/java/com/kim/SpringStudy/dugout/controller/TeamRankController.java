package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.domain.KBO;
import com.kim.SpringStudy.dugout.repository.KBORepository;
import com.kim.SpringStudy.dugout.service.KBOService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequiredArgsConstructor
//각 구단 순위에 관한 컨트롤러
public class TeamRankController {

    private final KBOService kboService;
    private final KBORepository kboRepository;

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

        return "games/teamRank";
    }

    //팀별 순위 시간선
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
}
