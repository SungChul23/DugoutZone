package com.kim.SpringStudy.dugout.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
//최종적으로 답변을 주는 클래스
public class ChatbotResponse {

    private String query; // 사용자가 질문한 원본 질문
    private String intent; // 어떠한 의도가 있는지 (ex 출생년도)
    private int count; // 해당 질문에 대한 결과 수
    private String summary; //자연어 설명
    private List<ChatbotResponseDTO> results; // ChatbotResponseDTO가 부모로서 결과값을 밷어냄
}
