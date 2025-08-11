package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.service.BatterStatsImportService;
import com.kim.SpringStudy.service.PitcherStatsImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/import")
public class BatterAndPitcherSaveController {

    private final BatterStatsImportService batterStatsImportService;
    private final PitcherStatsImportService pitcherStatsImportService;

    @PostMapping("/batter/{team}")
    public String importBatterStats(@PathVariable String team, @RequestBody String json)
            throws IOException {
        batterStatsImportService.importFromJson(team, json);
        return "✅ " + team + " 타자 기록 저장 완료";
    }

    @PostMapping("/pitcher/{team}")
    public String importPitcherStats(@PathVariable String team,
                                     @RequestBody List<Map<String, Object>> rows)
            throws IOException {
        pitcherStatsImportService.importPitcherStats(team,rows);
        return "✅ " + team + " 투수 기록 저장 완료";
    }

}
