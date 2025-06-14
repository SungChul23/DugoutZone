package com.kim.SpringStudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {


    @GetMapping("/list")
    String list(Model model) {
        model.addAttribute("name","김성철");
        model.addAttribute("money", "23억");
        return "list";
    }
}
