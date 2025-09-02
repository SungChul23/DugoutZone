package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.BatterAwardDTO;
import com.kim.SpringStudy.dugout.dto.PitcherAwardDTO;
import com.kim.SpringStudy.dugout.repository.BatterStatsRepository;
import com.kim.SpringStudy.dugout.repository.PitcherStatsRepository;
import com.kim.SpringStudy.dugout.service.KBOAwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
//수상 예축에 대한 컨트롤러
public class AwardController {

    private final KBOAwardService kboAwardService;
    private final BatterStatsRepository batterStatsRepository;
    private final PitcherStatsRepository pitcherStatsRepository;



    //25시즌 수상 메뉴
    @GetMapping("/award")
    public String showAward(){
        return "recordroom/award";
    }

    //타자 부분 수상 불러오기
    //안타왕, 타격왕, 홈런왕, 장타왕, 출루왕, 득점왕, 타점왕 ,도루왕(우선 제외)
    @GetMapping("/award/batter")
    @ResponseBody
    public Map<String, List<BatterAwardDTO>> getAllHitterAwardsJson() {
        return kboAwardService.getAllBatterAwards();
    }
    @GetMapping("/award/batter/view")
    public String getAllHitterAwardsPage(Model model) {
        //서비스에서 쿼리 조회(레포지) 후 결과값을 Map형태로 매핑한 컨트롤러로 토스
        model.addAttribute("awards", kboAwardService.getAllBatterAwards());

        //최신 데이터 표기
        LocalDate latestDate = batterStatsRepository.findLatestRecordDate();
        model.addAttribute("latestDate",latestDate);

        return "recordroom/batterAward";
    }

    // 투수 부분 수상 불러오기
    // era왕, 승률왕, 다승왕, 탈삼진왕, 세브왕, 홀드왕, 이닝왕
    @GetMapping("/award/pitcher")
    @RequestMapping
    public Map<String, List<PitcherAwardDTO>> getAllPitcherAwardsJson(){
        return kboAwardService.getAllPitcherAwards();
    }
    @GetMapping("/award/pitcher/view")
    public String getAllPitcherAwardsPage (Model model){
        //서비스에서 쿼리 조회(레포지) 후 결과값을 Map형태로 매핑한 컨트롤러로 토스
        model.addAttribute("awards", kboAwardService.getAllPitcherAwards());

        //최신 데이터 표기
        LocalDate latestDate = pitcherStatsRepository.findLatestRecordDate();
        model.addAttribute(("latestDate"), latestDate);

        return "recordroom/pitcherAward";
    }

    //골든 글러브
    @GetMapping("/award/goldenglobe")
    public String showGoldenGlobe(){
        return "recordroom/goldenglobe";
    }
}
