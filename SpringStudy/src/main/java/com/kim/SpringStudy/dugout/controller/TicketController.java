package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.domain.KBOTeam;
import com.kim.SpringStudy.dugout.repository.KBOTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//티켓
@Controller
@RequiredArgsConstructor
public class TicketController {


    private final KBOTeamRepository kboTeamRepository;

    //구단 별 예매 링크
    @GetMapping("/tickets")
    public String Tickets(Model model) {
        List<KBOTeam> result = kboTeamRepository.findAll();
        model.addAttribute("topTeams", result.subList(0, 5));
        model.addAttribute("bottomTeams", result.subList(5, 10));
        return "games/tickets";
    }
}
