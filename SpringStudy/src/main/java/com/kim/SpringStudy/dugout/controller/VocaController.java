package com.kim.SpringStudy.dugout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/voca")
@Controller
//야구 용어에 관한 컨트롤러
public class VocaController {

    @GetMapping
    public String showVoca() {
        return "baseballVoca/voca";
    }

    @GetMapping("/pitcher")
    public String showPicther() {
        return "baseballVoca/pitcherStats";
    }

    @GetMapping("/batter")
    public String showBatter() {
        return "baseballVoca/batterStats";
    }

    @GetMapping("/game")
    public String showGame() {
        return "baseballVoca/gameSituations";
    }

    @GetMapping("/position")
    public String showPosition() {
        return "baseballVoca/positionTerms";
    }

    @GetMapping("/extra")
    public String showGam() {
        return "baseballVoca/extraTerms";
    }
}
