package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.dto.GoldenGlovePlayerDTO;
import com.kim.SpringStudy.dugout.repository.GoldenGloveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldenGolveService {

    private final GoldenGloveRepository goldenGloveRepository;


    // 내야수/지명타자 각 포지션 1명만
    public GoldenGlovePlayerDTO topByPositionSub(String positionSub) {
        List<Object[]> rows = goldenGloveRepository.topPlayersByPositionSub(positionSub, 5);
        return rows.isEmpty() ? null : toDtoWithSub(rows.get(0), positionSub);
    }

    // 포지션 Sub 별 후보 5명 리스트
    public List<GoldenGlovePlayerDTO> candidatesByPositionSub(String positionSub) {
        return goldenGloveRepository.topPlayersByPositionSub(positionSub, 5).stream()
                .map(row -> toDtoWithSub(row, positionSub))
                .collect(Collectors.toList());
    }

    // 외야수: 3명 (좌중우) -> 단순히 3명만 리미트 걸고 가져옴
    public List<GoldenGlovePlayerDTO> topOutfielders() {
        return goldenGloveRepository.topOutfielders(3).stream()
                .map(row -> toDtoWithPos(row, "외야수"))
                .collect(Collectors.toList());
    }
    // 외야수 후보 5명 (하단용)
    public List<GoldenGlovePlayerDTO> outfielderCandidates() {
        return goldenGloveRepository.topOutfielders(5).stream()
                .map(row -> toDtoWithPos(row, "외야수"))
                .collect(Collectors.toList());
    }
    // 투수 후보 5
    public List<GoldenGlovePlayerDTO> topPitchers() {
        return goldenGloveRepository.topPitchers(5).stream()
                .map(row -> toDtoWithPos(row, "투수"))
                .collect(Collectors.toList());
    }
    // 포수 후보 5
    public List<GoldenGlovePlayerDTO> topCatchers() {
        return goldenGloveRepository.topCatchers(5).stream()
                .map(row -> toDtoWithPos(row, "포수"))
                .collect(Collectors.toList());
    }

    // position, position_sub 구별
    //내야수, 지명타자
    private GoldenGlovePlayerDTO toDtoWithSub(Object[] row, String positionSub) {
        String nameKr = (String) row[0];
        String team = (String) row[1];
        int score = ((Number) row[3]).intValue();
        return new GoldenGlovePlayerDTO(nameKr, team, null, positionSub, score);
    }

    //투수,포수,외야수
    private GoldenGlovePlayerDTO toDtoWithPos(Object[] row, String position) {
        String nameKr = (String) row[0];
        String team = (String) row[1];
        int score = ((Number) row[3]).intValue();
        return new GoldenGlovePlayerDTO(nameKr, team, position, null, score);
    }

}
