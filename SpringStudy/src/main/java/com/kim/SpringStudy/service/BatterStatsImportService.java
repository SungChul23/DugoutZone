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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatterStatsImportService {

    private final KBOplayerInfoRepository playerRepository;
    private final BatterStatsRepository batterStatsRepository;

    @Transactional
    public void importFromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> records = mapper.readValue(json, new TypeReference<>() {});

        for (Map<String, String> rec : records) {
            String playerName = rec.get("선수명").trim();

            // 선수 조회
            KBOplayerInfo player = playerRepository.findByNameKr(playerName)
                    .orElse(null);

            if (player == null) {
                System.out.println("⚠️ 선수 정보 없음 → 스킵: " + playerName);
                continue;
            }

            BatterStats stat = new BatterStats();
            stat.setPlayer(player);
            stat.setRecordDate(LocalDate.now());

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

            batterStatsRepository.save(stat);
        }
        System.out.println("✅ SSG 타자 기록 저장 완료");
    }

    private BigDecimal parseDecimal(String val) {
        if (val == null || val.equals("-") || val.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(val);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer parseInt(String val) {
        if (val == null || val.equals("-") || val.isBlank()) return 0;
        try {
            return Integer.valueOf(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
