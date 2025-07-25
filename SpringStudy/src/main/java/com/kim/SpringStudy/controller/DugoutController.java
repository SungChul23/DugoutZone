package com.kim.SpringStudy.controller;


import com.kim.SpringStudy.domain.KBO;
import com.kim.SpringStudy.domain.KBOTeam;
import com.kim.SpringStudy.repository.KBORepository;
import com.kim.SpringStudy.repository.KBOTeamRepository;
import com.kim.SpringStudy.service.KBOService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DugoutController {

    private final KBORepository kboRepository;
    private final KBOService kboService;
    private final KBOTeamRepository kboTeamRepository;

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
        model.addAttribute("teamNames" , teamNames);
        model.addAttribute("winRates", winRates);

        return "teamRank";
    }

    @GetMapping("/dugout")
    public String Dugout(){
        return "myKBO";
    }

    @GetMapping("/tickets")
    public String Tickets(Model model){
        List<KBOTeam> result = kboTeamRepository.findAll();
        model.addAttribute("topTeams", result.subList(0, 5));
        model.addAttribute("bottomTeams", result.subList(5, 10));
        return "tickets";
    }



}
