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

    @PostMapping("/ssg")
    public String importSSG(@RequestBody String json) throws IOException {
        batterStatsImportService.importFromJson(json);
        return "✅ SSG 타자 기록 저장 완료";
    }
}
