package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.domain.BatterStats;
import com.kim.SpringStudy.dugout.dto.BatterSearchDTO;
import com.kim.SpringStudy.dugout.repository.BatterStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatterService {

    private final BatterStatsRepository batterStatsRepository;

    public List<BatterStats> listByTeam(String teamCode, String q, String sort, String order) {
        Sort srt = buildSort(sort, order);

        LocalDate latestDate = batterStatsRepository.findLatestDateByTeam(teamCode);

        if (q != null && !q.isBlank()) {
            // 검색 시 최신 날짜만
            return batterStatsRepository.findByPlayer_TeamIgnoreCaseAndRecordDateAndPlayer_NameKrContaining(
                    teamCode, latestDate, q.trim(), srt);
        }

        // 기본 조회도 최신 날짜만
        return batterStatsRepository.findByPlayer_TeamIgnoreCaseAndRecordDate(teamCode, latestDate, srt);
    }

    public LocalDate getLatestDate(String teamCode) {
        return batterStatsRepository.findLatestDateByTeam(teamCode);
    }

    private Sort buildSort(String sort, String order) {
        String key = mapSortKey(sort);
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, key);
    }

    private String mapSortKey(String sort) {
        if (sort == null || sort.isBlank()) return "ops";
        return switch (sort) {
            case "nameKr", "team", "uniformNumber" -> sort;
            case "g","pa","ab","r","h","twoB","threeB","hr","tb","rbi","sac","sf","bb","ibb","hbp","so","gdp","mh" -> sort;
            case "avg","obp","slg","ops","risp","phBa" -> sort;
            default -> "ops";
        };
    }

    //10구단 모든 타자 검색
    public List<BatterSearchDTO> searchBatters(String keyword){
        return batterStatsRepository.searchByName(keyword);
    }
}

