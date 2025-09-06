package com.kim.SpringStudy.dugout.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BornPlayerDTO implements ChatbotResponseDTO{
    private String nameKr;
    private String team;
    private String position;
    private String birth;
}
