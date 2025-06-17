package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.domain.*;
import com.kim.SpringStudy.service.SSGService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SSGController {
    private final SSGRepository ssgRepository; //ssg 선수단 레포지 객체
    private final SSGGamesRepository ssgGamesRepository; //ssg 6월 경기 일정 레포지 객체
    private final SSGService ssgService; //ssg 서비스 객체

    @GetMapping("/list/ssg")
    String list(Model model) {
        List<SSG> result = ssgService.findPlayer();
        model.addAttribute("SSG", result);

        return "ssg.html";
    }

    @GetMapping("/list/ssg/games")
    String ListGames(Model model) {
        List<SSGGames> result = ssgService.findGames();
        model.addAttribute("SSGGames", result);

        return "ssggames.html";
    }

}
