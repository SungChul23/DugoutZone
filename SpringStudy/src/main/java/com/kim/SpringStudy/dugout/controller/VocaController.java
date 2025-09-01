package com.kim.SpringStudy.dugout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//야구 용어에 관한 컨트롤러
public class VocaController {

    @GetMapping("/voca")
    public String showVoca() {
        return "baseballVoca/voca";
    }

    @GetMapping("/voca/pitcher")
    public String showPicther() {
        return "baseballVoca/pitcherStats";
    }

    @GetMapping("/voca/batter")
    public String showBatter() {
        return "baseballVoca/batterStats";
    }

    @GetMapping("/voca/game")
    public String showGame() {
        return "baseballVoca/gameSituations";
    }

    @GetMapping("/voca/position")
    public String showPosition() {
        return "baseballVoca/positionTerms";
    }

    @GetMapping("/voca/extra")
    public String showGam() {
        return "baseballVoca/extraTerms";
    }
}
