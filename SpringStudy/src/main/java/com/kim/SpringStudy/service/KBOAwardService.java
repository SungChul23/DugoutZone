package com.kim.SpringStudy.service;

import com.kim.SpringStudy.dto.BatterAwardDTO;
import com.kim.SpringStudy.repository.BatterAwardRepository;
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

    public Map<String, List<BatterAwardDTO>> getAllAwards() {
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
                    (BigDecimal) row[4]  // BigDecimal 유지
            ));
        }
        return list;
    }
}

