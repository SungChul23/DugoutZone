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
        LocalDate today = LocalDate.now();

        for (Map<String, Object> row : rows) {
            String name = (String) row.get("선수명");

            // ✅ 크롤러가 보내는 키들
            String kboPlayerId = safeStr(row.get("kbo_player_id")); // 없을 수도 있음
            String birthRaw    = safeStr(row.get("birth"));
            String birth       = normalizeBirth(birthRaw); // "YYYY.MM.DD" 로 통일

            // ===== 1) 선수 찾기: playerId 최우선 =====
            KBOplayerInfo player = null;
            if (kboPlayerId != null && !kboPlayerId.isBlank()) {
                player = playerRepo.findByTeamAndKboPlayerId(team, kboPlayerId);
            }

            // ===== 2) 없으면 team+name+birth =====
            if (player == null && birth != null && !birth.isBlank()) {
                player = playerRepo.findByNameKrAndTeamAndBirth(name, team, birth);
            }

            // ===== 3) 그래도 없으면 team+name (단, 동명이인 여러 명이면 스킵) =====
            if (player == null) {
                List<KBOplayerInfo> cands = playerRepo.findAllByTeamAndNameKr(team, name);
                if (cands.size() == 1) {
                    player = cands.get(0);
                } else if (cands.size() > 1) {
                    System.out.println("⚠️ 동명이인 다수(식별자 부족) → 스킵: " + team + " / " + name);
                    continue; // 충돌 회피
                }
            }

            if (player == null) {
                System.out.println("⚠️ 선수 매칭 실패 → 스킵: " + team + " / " + name +
                        " / pid=" + kboPlayerId + " / birth=" + birth);
                continue;
            }

            // ✅ 오늘 중복저장 방지: 이름이 아니라 '선수' 기준으로 체크
            boolean exists = pitcherStatsRepo.existsByPlayerAndRecordDate(player, today);
            if (exists) {
                System.out.println("⏩ 이미 오늘 저장됨 → 스킵: " + team + " / " + name +
                        " / pid=" + kboPlayerId);
                continue;
            }

            // ===== 저장 =====
            PitcherStats stats = new PitcherStats();
            stats.setPlayer(player);
            stats.setRecordDate(today);

            // ----- Basic1 -----
            stats.setEra(toBigDecimal(row.get("ERA")));
            stats.setG(toInt(row.get("G")));
            stats.setW(toInt(row.get("W")));
            stats.setL(toInt(row.get("L")));
            stats.setSv(toInt(row.get("SV")));
            stats.setHld(toInt(row.get("HLD")));
            stats.setWpct(toBigDecimal(row.get("WPCT")));
            stats.setIp((String) row.getOrDefault("IP", null));
            stats.setH(toInt(row.get("H")));
            stats.setHr(toInt(row.get("HR")));
            stats.setBb(toInt(row.get("BB")));
            stats.setHbp(toInt(row.get("HBP")));
            stats.setSo(toInt(row.get("SO")));
            stats.setR(toInt(row.get("R")));
            stats.setEr(toInt(row.get("ER")));
            stats.setWhip(toBigDecimal(row.get("WHIP")));

            // ----- Basic2 -----
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

            pitcherStatsRepo.save(stats);
        }

        System.out.println("✅ " + team + " 투수 기록 저장 완료");
    }

    private String safeStr(Object v) {
        return v == null ? null : v.toString().trim();
    }

    // "YYYY.MM.DD" 로 통일
    private String normalizeBirth(String s) {
        if (s == null || s.isBlank()) return null;
        String only = s.replaceAll("[^0-9]", ""); // 숫자만
        if (only.length() >= 8) {
            return only.substring(0,4) + "." + only.substring(4,6) + "." + only.substring(6,8);
        }
        return null;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        try {
            String str = value.toString().trim();
            if (str.isEmpty() || str.equals("-") || str.equals("—")) return BigDecimal.ZERO;
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer toInt(Object value) {
        if (value == null) return 0;
        try {
            String str = value.toString().trim().replace(",", "");
            if (str.isEmpty() || str.equals("-") || str.equals("—")) return 0;
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}