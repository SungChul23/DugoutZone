// src/main/java/com/kim/SpringStudy/service/BatterService.java
package com.kim.SpringStudy.service;

import com.kim.SpringStudy.domain.BatterStats;
import com.kim.SpringStudy.repository.BatterStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatterService {

    private final BatterStatsRepository batterStatsRepository;

    public List<BatterStats> listByTeam(String teamCode, String q, String sort, String order) {
        Sort srt = buildSort(sort, order);
        if (q != null && !q.isBlank()) {
            return batterStatsRepository.findByPlayer_TeamIgnoreCaseAndPlayer_NameKrContaining(teamCode, q.trim(), srt);
        }
        return batterStatsRepository.findByPlayer_TeamIgnoreCase(teamCode, srt);
    }

    private Sort buildSort(String sort, String order) {
        String key = mapSortKey(sort);
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, key);
    }

    // 엔티티 필드명 화이트리스트 (네 엔티티 컬럼명에 맞춰 필요 시 수정)
    private String mapSortKey(String sort) {
        if (sort == null || sort.isBlank()) return "ops";
        return switch (sort) {
            // 메타
            case "nameKr", "team", "uniformNumber" -> sort;
            // 기본/카운팅
            case "g","pa","ab","r","h","twoB","threeB","hr","tb","rbi","sac","sf","bb","ibb","hbp","so","gdp","mh" -> sort;
            // 비율/지표
            case "avg","obp","slg","ops","risp","phBa" -> sort;
            default -> "ops";
        };
    }
}
