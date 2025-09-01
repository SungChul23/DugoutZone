package com.kim.SpringStudy.service;

import com.kim.SpringStudy.dto.BatterAwardDTO;
import com.kim.SpringStudy.dto.PitcherAwardDTO;
import com.kim.SpringStudy.repository.BatterAwardRepository;
import com.kim.SpringStudy.repository.PitcherAwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KBOAwardService {

    private final BatterAwardRepository batterAwardRepository;
    private final PitcherAwardRepository pitcherAwardRepository;

    public Map<String, List<BatterAwardDTO>> getAllBatterAwards() {
        Map<String, List<BatterAwardDTO>> result = new LinkedHashMap<>();

        result.put("홈런왕", convertInt(batterAwardRepository.getHR()));
        result.put("타점왕", convertInt(batterAwardRepository.getRBI()));
        result.put("안타왕", convertInt(batterAwardRepository.getH()));
        result.put("득점왕", convertInt(batterAwardRepository.getR()));

        result.put("타격왕", convertDecimal(batterAwardRepository.getAVG()));
        result.put("출루율왕", convertDecimal(batterAwardRepository.getOBP()));
        result.put("장타율왕", convertDecimal(batterAwardRepository.getSLG()));

        return result;
    }

    // 정수용 DTO 변환 (안타, 홈런, 타점, 득점)
    private List<BatterAwardDTO> convertInt(List<Object[]> rawList) {
        List<BatterAwardDTO> list = new ArrayList<>();
        for (Object[] row : rawList) {
            list.add(new BatterAwardDTO(
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    ((Number) row[4]).intValue()  // int 처리
            ));
        }
        return list;
    }

    // 소수용 DTO 변환
    private List<BatterAwardDTO> convertDecimal(List<Object[]> rawList) {
        List<BatterAwardDTO> list = new ArrayList<>();
        for (Object[] row : rawList) {
            list.add(new BatterAwardDTO(
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (BigDecimal) row[4]
            ));
        }
        return list;
    }

    //투수 부분
    public Map<String, List<PitcherAwardDTO>> getAllPitcherAwards() {
        Map<String, List<PitcherAwardDTO>> result = new LinkedHashMap<>();
        result.put("다승왕", convertPitcher(pitcherAwardRepository.getW()));
        result.put("탈삼진왕", convertPitcher(pitcherAwardRepository.getSO()));
        result.put("세이브왕", convertPitcher(pitcherAwardRepository.getSV()));
        result.put("홀드왕", convertPitcher(pitcherAwardRepository.getHLD()));
        result.put("이닝왕", convertPitcher(pitcherAwardRepository.getIP()));
        result.put("평균자책점왕", convertPitcher(pitcherAwardRepository.getERA()));
        result.put("승률왕", convertPitcher(pitcherAwardRepository.getWPCT()));
        return result;
    }
    private List<PitcherAwardDTO> convertPitcher(List<Object[]> rawList) {
        List<PitcherAwardDTO> list = new ArrayList<>();
        int rank = 1;
        for (Object[] row : rawList) {
            list.add(new PitcherAwardDTO(
                    rank++,
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    row[3]  // int, String, BigDecimal 모두 수용
            ));
        }
        return list;
    }
}

