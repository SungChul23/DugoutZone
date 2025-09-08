package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.chatbot.ChatbotResponse;
import com.kim.SpringStudy.dugout.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatbot")

//룰-기반 챗봇임
public class ChatbotController {

    private final ChatbotService chatbotService;

    // HTML 렌더링용
    @GetMapping
    public String chatbotPage() {
        return "chatbot/chatbotResult"; // templates/chatbot.html
    }

    // JSON 응답용  -> 뷰 에서 요청
    // 파람에 들어온 값을 통해 사용자의 의도 (intent)를 파악하자
    @GetMapping("/query")
    @ResponseBody
    public ChatbotResponse handleQuery(@RequestParam String text) {
        return chatbotService.process(text);
    }
}