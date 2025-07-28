package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.domain.*;
import com.kim.SpringStudy.repository.SSGGamesRepository;
import com.kim.SpringStudy.repository.SSGRepository;
import com.kim.SpringStudy.service.SSGService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SSGController {
    private final SSGRepository ssgRepository; //ssg 선수단 레포지 객체
    private final SSGGamesRepository ssgGamesRepository; //ssg 6월 경기 일정 레포지 객체
    private final SSGService ssgService; //ssg 서비스 객체

    //전체 목록
    @GetMapping("/list/ssg")
    String ssglist(Model model) {
        List<SSG> result = ssgService.findPlayer();
        model.addAttribute("SSG", result);

        return "ssgv2.html";
    }
    //선수단 목록 페이지네이션
    @GetMapping("/list/ssg/page/{pageNum}")
    public String ssgListGet(@PathVariable ("pageNum") int pageNum, Model model){
        Page<SSG> result = ssgRepository.findAll(PageRequest.of(pageNum-1,8));
        model.addAttribute("SSG", result.getContent());  // 페이징된 데이터만 넘김
        model.addAttribute("currentPage", pageNum);       // 현재 페이지 정보 전달
        model.addAttribute("totalPages", result.getTotalPages()); // 총 페이지 수 전달
        return "ssg_Page";
    }
    //ssg 시합 날짜
    @GetMapping("/list/ssg/games")
    public String ListGames(Model model) {
        List<SSGGames> result = ssgService.findGames();
        model.addAttribute("SSGGames", result);

        return "ssggames.html";
    }
    // 선수단 검색 기능
    @PostMapping("/searchPlayer")
    public String searchPlayer(@RequestParam String player , Model model){
        var result = ssgRepository.rawQuery(player);
        model.addAttribute("SSG" ,result);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        System.out.println("출력문: " + result);

        return "ssg_Page";
    }

}
