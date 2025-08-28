package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.dto.PlayerPositionDTO;
import com.kim.SpringStudy.service.BatterStatsImportService;
import com.kim.SpringStudy.service.PitcherStatsImportService;
import com.kim.SpringStudy.service.PlayerPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/import")
public class KBOcrawlerControaller {

    private final BatterStatsImportService batterStatsImportService;
    private final PitcherStatsImportService pitcherStatsImportService;
    private final PlayerPositionService playerPositionService;

    //타자기록 import
    @PostMapping("/batter/{team}")
    public String importBatterStats(@PathVariable String team, @RequestBody String json)
            throws IOException {
        batterStatsImportService.importFromJson(team, json);
        return team  + "-> 타자 기록 저장 완료";
    }
    //투수기록 import
    @PostMapping("/pitcher/{team}")
    public String importPitcherStats(@PathVariable String team,
                                     @RequestBody List<Map<String, Object>> rows)
            throws IOException {
        pitcherStatsImportService.importPitcherStats(team,rows);
        return team  + "-> 투수 기록 저장 완료";
    }

    //선수 포지션 구체화 import
    @PostMapping("/position-sub")
    public ResponseEntity<String> importPositionSub(@RequestBody PlayerPositionDTO dto) {
        return playerPositionService.updatePositionSub(dto);
    }


}
