package com.kim.SpringStudy.dugout.controller;

import org.springframework.ui.Model;
import com.kim.SpringStudy.dugout.dto.ChatbotResponse;
import com.kim.SpringStudy.dugout.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    // HTML 렌더링용
    @GetMapping
    public String chatbotPage() {
        return "chatbot/chatbotResult"; // templates/chatbot.html
    }

    // JSON 응답용 (기존 API 유지)
    @GetMapping("/query")
    @ResponseBody
    public ChatbotResponse handleQuery(@RequestParam String text) {
        return chatbotService.process(text);
    }
}