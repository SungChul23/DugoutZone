package com.kim.SpringStudy.dugout.dto.chatbot;


import com.kim.SpringStudy.dugout.dto.chatbot.ChatbotResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsPlayerDTO implements ChatbotResponseDTO {
    private String nameKr; //이름
    private String team; //팀
    private String position; //포지션
    private String statType; // 지표 이름 (HR, AVG …)
    private String statValue; // 값 (20, 0.345 …)
}
