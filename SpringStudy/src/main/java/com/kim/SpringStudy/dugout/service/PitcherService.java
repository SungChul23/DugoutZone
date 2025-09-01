package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.domain.PitcherStats;
import com.kim.SpringStudy.dugout.dto.PitcherSearchDTO;
import com.kim.SpringStudy.dugout.repository.PitcherStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PitcherService {

    private final PitcherStatsRepository pitcherStatsRepository;

    public List<PitcherStats> listByTeam(String teamCode, String q, String sort, String order) {
        Sort srt = buildSort(sort, order);

        LocalDate latestDate = pitcherStatsRepository.findLatestDateByTeam(teamCode);
        if (latestDate == null) {
            // 해당 팀의 데이터가 아직 없을 때 안전 처리
            return List.of();
        }

        // 검색 시에도 최신 날짜 한정
        if (q != null && !q.isBlank()) {
            return pitcherStatsRepository
                    .findByPlayer_TeamIgnoreCaseAndRecordDateAndPlayer_NameKrContaining(
                            teamCode, latestDate, q.trim(), srt
                    );
        }

        // 기본 조회: 최신 날짜 한정
        return pitcherStatsRepository
                .findByPlayer_TeamIgnoreCaseAndRecordDate(teamCode, latestDate, srt);
    }

    // 팀별 최신 데이터
    public LocalDate getLatestDate(String teamCode) {
        return pitcherStatsRepository.findLatestDateByTeam(teamCode);
    }

    //정렬 객체 생성
    private Sort buildSort(String sort, String order) {
        String key = mapSortKey(sort);
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, key);
    }

    //요청 정렬 키 → 엔티티 필드명 매핑
    private String mapSortKey(String sort) {
        if (sort == null || sort.isBlank()) return "era"; // 기본값: ERA
        return switch (sort) {
            // 이름/팀/등번호를 정렬키로 쓰는 경우가 있다면(HTML 옵션 사용 시)
            case "nameKr", "team", "uniformNumber" -> sort;

            // ===== 기본 기록 (Basic1)
            case "era", "g", "w", "l", "sv", "hld", "wpct",
                    "ip", // 주의: String 정렬(lexicographical) — 필요시 DB 뷰/가상컬럼으로 numeric 변환 고려
                    "h", "hr", "bb", "hbp", "so", "r", "er", "whip" -> sort;

            // ===== 세부 기록 (Basic2)
            case "cg", "sho", "qs", "bsv", "tbf", "np", "avg",
                    "twoB", "threeB", "sac", "sf", "ibb", "wp", "bk" -> sort;

            default -> "era";
        };
    }

    //10구단 모든 투수 검색
    public List<PitcherSearchDTO> searchPitchers(String keyword){
        return pitcherStatsRepository.searchByName(keyword);
    }
}
