package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.service.BatterStatsImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/import")
public class BatterStatsImportController {

    private final BatterStatsImportService batterStatsImportService;

    @PostMapping("/{team}")
    public String importBatterStats(@PathVariable String team, @RequestBody String json)
            throws IOException {
        batterStatsImportService.importFromJson(team, json);
        return "✅ " + team + " 타자 기록 저장 완료";
    }
}
