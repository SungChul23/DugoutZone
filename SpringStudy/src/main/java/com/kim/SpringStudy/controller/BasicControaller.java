package com.kim.SpringStudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicControaller {
    @GetMapping("/")
    String apple() {
        return "index";
    }

    @GetMapping("/mypage")
    @ResponseBody
    String apple2() {
        return "마이페이지 입니다";
    }


}
