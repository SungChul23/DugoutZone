package com.kim.SpringStudy.dugout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    //더그아웃 입장
    @GetMapping("/dugout")
    public String Dugout() {
        return "dugout";
    }
}
