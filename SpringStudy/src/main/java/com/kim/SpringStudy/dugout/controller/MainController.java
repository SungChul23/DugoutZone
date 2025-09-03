package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.domain.KBOTeam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    //더그아웃 입장
    @GetMapping("/dugout")
    public String Dugout() {
        return "dugout";
    }

}
