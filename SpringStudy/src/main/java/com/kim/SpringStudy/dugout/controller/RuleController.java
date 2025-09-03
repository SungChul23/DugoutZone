package com.kim.SpringStudy.dugout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rules")
//야구 룰 설명에 관한 컨틀롤러
public class RuleController {

    @GetMapping
    public String showRules() {
        return "baseballRules/rules";
    }

    @GetMapping("/quickstart")
    public String showQuickstart() {
        return "baseballRules/quickstart";
    }

    @GetMapping("/offense")
    public String showOffense() {
        return "baseballRules/offense";
    }

    @GetMapping("/defense")
    public String showDefense() {
        return "baseballRules/defense";
    }

    @GetMapping("/baserun")
    public String showBaserun() {
        return "baseballRules/baserun";
    }

    @GetMapping("/pitching")
    public String showPitching() {
        return "baseballRules/pitching";
    }

    @GetMapping("/calls")
    public String showCalls() {
        return "baseballRules/calls";
    }

    @GetMapping("/kbo")
    public String showKBO() {
        return "baseballRules/kbo";
    }

    @GetMapping("/mis")
    public String showMis() {
        return "baseballRules/mis";
    }

}
