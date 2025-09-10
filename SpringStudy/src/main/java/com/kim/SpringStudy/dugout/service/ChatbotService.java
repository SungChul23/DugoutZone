package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.dto.chatbot.BornPlayerDTO;
import com.kim.SpringStudy.dugout.dto.chatbot.ChatbotResponse;
import com.kim.SpringStudy.dugout.dto.chatbot.ChatbotResponseDTO;
import com.kim.SpringStudy.dugout.dto.chatbot.StatsPlayerDTO;
import com.kim.SpringStudy.dugout.repository.ChatbotResponseRepo;
import com.kim.SpringStudy.dugout.util.ChatbotTeamNameMapper;
import com.kim.SpringStudy.dugout.util.YearResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final YearResolver yearResolver;
    private final ChatbotResponseRepo playerRepository;

    private static final List<String> POSITIONS = List.of("투수", "포수", "내야수", "외야수");


    // 메인 엔트리
    public ChatbotResponse process(String text) {
        if (text.contains("년생")) {
            return processBornYear(text);
        } else if (containsStatCondition(text)) {
            return processCondition(text);
        } else {
            return new ChatbotResponse(
                    text, "UNKNOWN", 0,
                    "아직 이해하지 못한 질문이에요.",
                    List.of()
            );
        }
    }

    // 1) 출생년도 조회
    private ChatbotResponse processBornYear(String text) {
        Optional<Integer> yearOpt = yearResolver.resolveYear(text);
        if (yearOpt.isEmpty()) {
            return new ChatbotResponse(text, "BORN_YEAR_PLAYER", 0, "연도를 인식할 수 없어요.", List.of());
        }

        String year = String.valueOf(yearOpt.get());
        String position = POSITIONS.stream().filter(text::contains).findFirst().orElse(null);
        String team = ChatbotTeamNameMapper.resolve(text); //팀 매핑 적용

        List<Map<String, Object>> rows = playerRepository.findBornPlayers(year, team, position);

        List<ChatbotResponseDTO> results = rows.stream()
                .map(row -> new BornPlayerDTO(
                        (String) row.get("nameKr"),
                        (String) row.get("team"),
                        (String) row.get("position"),
                        (String) row.get("birth")
                ))
                .map(dto -> (ChatbotResponseDTO) dto)
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

    // 2) 조건형 기록 조회 (타자 전용)
    private ChatbotResponse processCondition(String text) {
        String team = ChatbotTeamNameMapper.resolve(text);
        String statType = extractStatType(text);   // 홈런, 타율, OPS …
        double value = extractConditionValue(text);

        List<Map<String, Object>> rows =
                playerRepository.findBatterByCondition(team, statType, value);

        List<ChatbotResponseDTO> results = rows.stream()
                .map(r -> new StatsPlayerDTO(
                        (String) r.get("nameKr"),
                        (String) r.get("team"),
                        (String) r.get("position"),
                        statType,
                        String.valueOf(r.get(statType.toLowerCase()))
                ))
                .map(dto -> (ChatbotResponseDTO) dto)
                .toList();

        String summary = String.format("%s %s %.3f 이상 타자는 %d명입니다: %s",
                team != null ? team : "리그 전체",
                statType,
                value,
                results.size(),
                results.stream()
                        .map(r -> ((StatsPlayerDTO) r).getNameKr())
                        .limit(5)
                        .collect(Collectors.joining(", "))
        );

        return new ChatbotResponse(text, "BATTING_CONDITION_PLAYER", results.size(), summary, results);
    }

    // 유틸 메서드 시작

    private boolean containsStatCondition(String text) {
        return text.contains("이상") || text.contains("이하") || text.contains("넘는");
    }

    private String extractStatType(String text) {
        if (text.contains("홈런")) return "HR";
        if (text.contains("타점")) return "RBI";
        if (text.contains("득점")) return "R";
        if (text.contains("안타")) return "H";
        if (text.contains("삼진")) return "SO";
        if (text.contains("병살")) return "GDP";
        if (text.contains("타율")) return "AVG";
        if (text.contains("출루율")) return "OBP";
        if (text.contains("장타율")) return "SLG";
        if (text.contains("OPS")) return "OPS";
        return "HR"; // 기본값
    }

    private double extractConditionValue(String text) {
        Pattern numPattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher matcher = numPattern.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0;
    }
}
