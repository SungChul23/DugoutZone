package com.kim.SpringStudy.dugout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//야구 룰 설명에 관한 컨틀롤러
public class RuleController {

    @GetMapping("/rules")
    public String showRules() {
        return "baseballRules/rules";
    }

    @GetMapping("/rules/quickstart")
    public String showQuickstart() {
        return "baseballRules/quickstart";
    }

    @GetMapping("/rules/offense")
    public String showOffense() {
        return "baseballRules/offense";
    }

    @GetMapping("/rules/defense")
    public String showDefense() {
        return "baseballRules/defense";
    }

    @GetMapping("/rules/baserun")
    public String showBaserun() {
        return "baseballRules/baserun";
    }

    @GetMapping("/rules/pitching")
    public String showPitching() {
        return "baseballRules/pitching";
    }

    @GetMapping("/rules/calls")
    public String showCalls() {
        return "baseballRules/calls";
    }

    @GetMapping("/rules/kbo")
    public String showKBO() {
        return "baseballRules/kbo";
    }

    @GetMapping("/rules/mis")
    public String showMis() {
        return "baseballRules/mis";
    }

}
