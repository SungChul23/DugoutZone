package com.kim.SpringStudy.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class BasicControaller {
    @GetMapping("/")
    String apple(Model model, Principal principal) {

        //현재 로그인 한 상태
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        }
        return "index";
    }
}
