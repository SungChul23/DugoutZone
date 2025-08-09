package com.kim.SpringStudy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.SpringStudy.domain.BatterStats;
import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.repository.BatterStatsRepository;
import com.kim.SpringStudy.repository.KBOplayerInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BatterStatsImportService {

    private final KBOplayerInfoRepository playerRepository;
    private final BatterStatsRepository batterStatsRepository;

    private static final List<String> BATTER_POS =
            List.of("포수", "내야수", "외야수", "지명타자");

    @Transactional
    public void importFromJson(String team, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> records = mapper.readValue(json, new TypeReference<>() {});
        LocalDate today = LocalDate.now();

        // 요청 내 같은 선수 중복 방지
        Set<Long> seenInRequest = new HashSet<>();

        for (Map<String, String> rec : records) {
            String playerName = rec.getOrDefault("선수명", "").trim();
            if (playerName.isEmpty()) continue;

            // 1) 배터 포지션 우선 매칭
            KBOplayerInfo player = playerRepository
                    .findFirstByNameKrAndTeamAndPositionIn(playerName, team, BATTER_POS)
                    .orElse(null);

            // 2) 못 찾으면 전체 후보에서 규칙으로 1명 선택
            if (player == null) {
                List<KBOplayerInfo> candidates = playerRepository.findAllByNameKrAndTeam(playerName, team);
                if (candidates.isEmpty()) {
                    System.out.println("⚠️ 선수 정보 없음 → 스킵: " + team + " / " + playerName);
                    continue;
                }
                // 규칙: 배터이면 우선, 동률이면 id 오름차순
                player = candidates.stream()
                        .sorted((a, b) -> {
                            boolean aIsBatter = BATTER_POS.contains(a.getPosition());
                            boolean bIsBatter = BATTER_POS.contains(b.getPosition());
                            if (aIsBatter != bIsBatter) return aIsBatter ? -1 : 1;
                            return Long.compare(a.getId(), b.getId());
                        })
                        .findFirst().get();
                System.out.println("ℹ️ 다중 후보 → 선택: " + playerName + " / " + player.getPosition());
            }

            // 3) 투수면 스킵 (혹시 배터 우선 매칭을 못 탔다면)
            String pos = player.getPosition();
            if (pos != null && pos.contains("투수")) {
                System.out.println("↩️ 투수 발견, 배터 저장 스킵: " + playerName + " (" + pos + ")");
                continue;
            }

            // 4) 요청 내 중복 방지
            if (!seenInRequest.add(player.getId())) {
                System.out.println("↩️ 요청 내 중복 → 스킵: " + playerName);
                continue;
            }

            // 5) 같은 날짜 중복 방지 (스킵 또는 업서트 택1)
            if (batterStatsRepository.existsByPlayerAndRecordDate(player, today)) {
                System.out.println("↩️ 이미 저장됨(" + today + ") → 스킵: " + playerName);
                // 업서트 원하면 아래로 대체:
                // BatterStats stat = batterStatsRepository.findByPlayerAndRecordDate(player, today).get();
                // fill(stat, rec); batterStatsRepository.save(stat);
                continue;
            }

            // 6) 신규 저장
            BatterStats stat = new BatterStats();
            stat.setPlayer(player);
            stat.setRecordDate(today);
            // === 필드 매핑 ===
            stat.setAvg(parseDecimal(rec.get("AVG")));
            stat.setG(parseInt(rec.get("G")));
            stat.setPa(parseInt(rec.get("PA")));
            stat.setAb(parseInt(rec.get("AB")));
            stat.setR(parseInt(rec.get("R")));
            stat.setH(parseInt(rec.get("H")));
            stat.setTwoB(parseInt(rec.get("2B")));
            stat.setThreeB(parseInt(rec.get("3B")));
            stat.setHr(parseInt(rec.get("HR")));
            stat.setTb(parseInt(rec.get("TB")));
            stat.setRbi(parseInt(rec.get("RBI")));
            stat.setSac(parseInt(rec.get("SAC")));
            stat.setSf(parseInt(rec.get("SF")));
            stat.setBb(parseInt(rec.get("BB")));
            stat.setIbb(parseInt(rec.get("IBB")));
            stat.setHbp(parseInt(rec.get("HBP")));
            stat.setSo(parseInt(rec.get("SO")));
            stat.setGdp(parseInt(rec.get("GDP")));
            stat.setSlg(parseDecimal(rec.get("SLG")));
            stat.setObp(parseDecimal(rec.get("OBP")));
            stat.setOps(parseDecimal(rec.get("OPS")));
            stat.setMh(parseInt(rec.get("MH")));
            stat.setRisp(parseDecimal(rec.get("RISP")));
            stat.setPhBa(parseDecimal(rec.get("PH-BA")));
            // =================

            batterStatsRepository.save(stat);
        }
        System.out.println("✅ " + team + " 타자 기록 저장 완료");
    }

    private BigDecimal parseDecimal(String val) {
        if (val == null || val.isBlank() || "-".equals(val)) return BigDecimal.ZERO;
        try { return new BigDecimal(val); } catch (Exception e) { return BigDecimal.ZERO; }
    }
    private Integer parseInt(String val) {
        if (val == null || val.isBlank() || "-".equals(val)) return 0;
        try { return Integer.valueOf(val); } catch (Exception e) { return 0; }
    }
}
