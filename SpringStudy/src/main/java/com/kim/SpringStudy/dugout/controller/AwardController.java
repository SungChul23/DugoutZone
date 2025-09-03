package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.BatterAwardDTO;
import com.kim.SpringStudy.dugout.dto.GoldenGlovePlayerDTO;
import com.kim.SpringStudy.dugout.dto.PitcherAwardDTO;
import com.kim.SpringStudy.dugout.repository.BatterStatsRepository;
import com.kim.SpringStudy.dugout.repository.GoldenGloveRepository;
import com.kim.SpringStudy.dugout.repository.PitcherStatsRepository;
import com.kim.SpringStudy.dugout.service.GoldenGolveService;
import com.kim.SpringStudy.dugout.service.KBOAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.attribute.Attribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/award") // 클래스 레벨에서 공통 경로 설정
public class AwardController {

    private final KBOAwardService kboAwardService;
    private final BatterStatsRepository batterStatsRepository;
    private final PitcherStatsRepository pitcherStatsRepository;
    private final GoldenGolveService goldenGolveService;

    //25시즌 수상 메뉴
    @GetMapping
    public String showAward() {
        return "recordroom/award";
    }

    // 타자 부분 수상 불러오기
    @GetMapping("/batter")
    @ResponseBody
    public Map<String, List<BatterAwardDTO>> getAllHitterAwardsJson() {
        return kboAwardService.getAllBatterAwards();
    }

    @GetMapping("/batter/view")
    public String getAllHitterAwardsPage(Model model) {
        model.addAttribute("awards", kboAwardService.getAllBatterAwards());

        LocalDate latestDate = batterStatsRepository.findLatestRecordDate();
        model.addAttribute("latestDate", latestDate);

        return "recordroom/batterAward";
    }

    // 투수 부분 수상 불러오기
    @GetMapping("/pitcher")
    @ResponseBody
    public Map<String, List<PitcherAwardDTO>> getAllPitcherAwardsJson() {
        return kboAwardService.getAllPitcherAwards();
    }

    @GetMapping("/pitcher/view")
    public String getAllPitcherAwardsPage(Model model) {
        model.addAttribute("awards", kboAwardService.getAllPitcherAwards());

        LocalDate latestDate = pitcherStatsRepository.findLatestRecordDate();
        model.addAttribute("latestDate", latestDate);

        return "recordroom/pitcherAward";
    }

    // 골든 글러브
    @GetMapping("/goldenglove")
    public String showGoldenGlobe(Model model) {


        model.addAttribute("goldenGlovePitchers",
                goldenGolveService.topPitchers());
        model.addAttribute("goldenGloveCatchers",
                goldenGolveService.topCatchers());
        model.addAttribute("goldenGloveOutfielders",  //외야수
                goldenGolveService.topOutfielders());

        List<GoldenGlovePlayerDTO> infielders = new ArrayList<>();
        addIfNotNull(infielders, goldenGolveService.topByPositionSub("1루수"));
        addIfNotNull(infielders, goldenGolveService.topByPositionSub("2루수"));
        addIfNotNull(infielders, goldenGolveService.topByPositionSub("3루수"));
        addIfNotNull(infielders, goldenGolveService.topByPositionSub("유격수"));
        //addIfNotNull(infielders, goldenGolveService.topByPositionSub("지명타자"));
        model.addAttribute("goldenGloveInfielders", infielders);


        return "recordroom/goldenglove";
    }

    //포지션 없으면 null 반환
    private void addIfNotNull(List<GoldenGlovePlayerDTO> list, GoldenGlovePlayerDTO dto) {
        if (dto != null) list.add(dto);
    }
}