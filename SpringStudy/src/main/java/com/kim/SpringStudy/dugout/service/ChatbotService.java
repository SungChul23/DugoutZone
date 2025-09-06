package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.dto.BornPlayerDTO;
import com.kim.SpringStudy.dugout.dto.ChatbotResponse;
import com.kim.SpringStudy.dugout.dto.ChatbotResponseDTO;
import com.kim.SpringStudy.dugout.repository.ChatbotResponseRepo;
import com.kim.SpringStudy.dugout.util.YearResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatbotService {


    private final YearResolver yearResolver;
    private final ChatbotResponseRepo playerRepository;


    private static final List<String> POSITIONS = List.of("투수", "포수", "내야수", "외야수");
    private static final List<String> TEAMS = List.of("LG", "SSG", "KIA", "KT", "삼성", "NC", "두산", "키움", "롯데", "한화");


    public ChatbotResponse process(String text) {
        Optional<Integer> yearOpt = yearResolver.resolveYear(text);
        if (yearOpt.isEmpty()) {
            return new ChatbotResponse(text, "BORN_YEAR_PLAYER", 0, "연도를 인식할 수 없어요.", List.of());
        }


        String year = String.valueOf(yearOpt.get());
        String position = POSITIONS.stream().filter(text::contains).findFirst().orElse(null);
        String team = TEAMS.stream().filter(text::contains).findFirst().orElse(null);


        List<Map<String, Object>> rows = playerRepository.findBornPlayers(year, team, position);
        List<ChatbotResponseDTO> results = rows.stream()
                .map(row -> new BornPlayerDTO(
                        (String) row.get("nameKr"),
                        (String) row.get("team"),
                        (String) row.get("position"),
                        (String) row.get("birth")
                ))
                .map(dto -> (ChatbotResponseDTO) dto)  //명시적 업캐스팅
                .toList();


        String summary = generateSummary(year, team, position, results);


        return new ChatbotResponse(text, "BORN_YEAR_PLAYER", results.size(), summary, results);
    }


    private String generateSummary(String year, String team, String position, List<ChatbotResponseDTO> results) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("년생");
        if (team != null) sb.append(" ").append(team);
        if (position != null) sb.append(" ").append(position);
        sb.append("는 총 ").append(results.size()).append("명입니다: ");


        List<String> names = results.stream()
                .map(r -> ((BornPlayerDTO) r).getNameKr())
                .limit(5)
                .toList();


        sb.append(String.join(", ", names));


        if (results.size() > 5) {
            sb.append(" 외 ").append(results.size() - 5).append("명");
        }


        return sb.toString();
    }
}