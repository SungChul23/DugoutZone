package com.kim.SpringStudy.service;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.domain.PitcherStats;
import com.kim.SpringStudy.repository.KBOplayerInfoRepository;
import com.kim.SpringStudy.repository.PitcherStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PitcherStatsImportService {

    private final KBOplayerInfoRepository playerRepo;
    private final PitcherStatsRepository pitcherStatsRepo;

    @Transactional
    public void importPitcherStats(String team, List<Map<String, Object>> rows) {

        LocalDate today = LocalDate.now(); // 오늘 날짜

        for (Map<String, Object> row : rows) {
            String playerName = (String) row.get("선수명");

            // 오늘 데이터가 이미 존재하면 스킵
            boolean exists = pitcherStatsRepo.existsByPlayer_NameKrAndPlayer_TeamAndRecordDate(playerName, team, today);
            if (exists) {
                System.out.println("⏩ 이미 오늘 저장됨 → 스킵: " + team + " / " + playerName);
                continue;
            }

            KBOplayerInfo player = null;

            if ("삼성".equals(team)) {
                // 삼성 → 생년월일 기반 검색
                String birth = (String) row.get("birth");
                if (birth != null && !birth.isBlank()) {
                    player = playerRepo.findByNameKrAndTeamAndBirth(playerName, team, birth);
                }
            } else {
                // 나머지 구단 → 이름 + 팀으로만 검색
                player = playerRepo.findByNameKrAndTeam(playerName, team);
            }

            // 선수 못 찾으면 스킵
            if (player == null) {
                System.out.println("⚠️ 선수 정보 없음 → 스킵: " + team + " / " + playerName);
                continue;
            }

            // PitcherStats 객체 생성
            PitcherStats stats = new PitcherStats();
            stats.setPlayer(player);
            stats.setRecordDate(today);

// ===== 기본 기록 (Basic1) =====
            stats.setEra(toBigDecimal(row.get("ERA")));
            stats.setG(toInt(row.get("G")));
            stats.setW(toInt(row.get("W")));       // 승
            stats.setL(toInt(row.get("L")));       // 패
            stats.setSv(toInt(row.get("SV")));     // 세이브
            stats.setHld(toInt(row.get("HLD")));   // 홀드
            stats.setWpct(toBigDecimal(row.get("WPCT")));
            stats.setIp((String) row.get("IP"));
            stats.setH(toInt(row.get("H")));
            stats.setHr(toInt(row.get("HR")));
            stats.setBb(toInt(row.get("BB")));
            stats.setHbp(toInt(row.get("HBP")));
            stats.setSo(toInt(row.get("SO")));
            stats.setR(toInt(row.get("R")));
            stats.setEr(toInt(row.get("ER")));
            stats.setWhip(toBigDecimal(row.get("WHIP")));

// ===== 세부 기록 (Basic2) =====
            stats.setCg(toInt(row.get("CG")));
            stats.setSho(toInt(row.get("SHO")));
            stats.setQs(toInt(row.get("QS")));
            stats.setBsv(toInt(row.get("BSV")));
            stats.setTbf(toInt(row.get("TBF")));
            stats.setNp(toInt(row.get("NP")));
            stats.setAvg(toBigDecimal(row.get("AVG")));
            stats.setTwoB(toInt(row.get("2B")));
            stats.setThreeB(toInt(row.get("3B")));
            stats.setSac(toInt(row.get("SAC")));
            stats.setSf(toInt(row.get("SF")));
            stats.setIbb(toInt(row.get("IBB")));
            stats.setWp(toInt(row.get("WP")));
            stats.setBk(toInt(row.get("BK")));

            // 저장
            pitcherStatsRepo.save(stats);
        }

        System.out.println("✅ " + team + " 투수 기록 저장 완료");
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        try {
            String str = value.toString().trim();
            if (str.isEmpty() || str.equals("-") || str.equals("—")) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer toInt(Object value) {
        if (value == null) return 0;
        try {
            String str = value.toString().trim().replace(",", "");
            if (str.isEmpty() || str.equals("-") || str.equals("—")) {
                return 0;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
