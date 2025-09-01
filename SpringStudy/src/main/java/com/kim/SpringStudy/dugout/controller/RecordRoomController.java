package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.BatterSearchDTO;
import com.kim.SpringStudy.dugout.dto.PitcherSearchDTO;
import com.kim.SpringStudy.dugout.repository.BatterStatsRepository;
import com.kim.SpringStudy.dugout.repository.PitcherStatsRepository;
import com.kim.SpringStudy.dugout.service.BatterService;
import com.kim.SpringStudy.dugout.service.PitcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
//기록실에 관한 컨트롤러
public class RecordRoomController {


    private final BatterService batterService;
    private final BatterStatsRepository batterStatsRepository;

    private final PitcherService pitcherService;
    private final PitcherStatsRepository pitcherStatsRepository;

    // 10구단 타자 기록실
    @GetMapping("/allrecords")
    public String showAllRecord() {
        return "recordroom/allrecords";
    }

    //기록실 기능 - 10구단 타자 검색
    @GetMapping("/allrecords/batter")
    public String searchBatter(@RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {

        List<BatterSearchDTO> result;
        //이름이 널이 아니아고 공백이 아니라면
        if (keyword != null && !keyword.isBlank()) {
            result = batterService.searchBatters(keyword);
        } else {
            result = List.of(); //빈리스트 반환
        }
        //최신 기록 가져오기
        LocalDate latestDate = batterStatsRepository.findLatestRecordDate();

        model.addAttribute("name", keyword);
        model.addAttribute("batterList", result);
        model.addAttribute("latestDate", latestDate);

        return "recordroom/allbatter";
    }

    //기록실 기능 - 10구단 투수 검색
    @GetMapping("/allrecords/pitcher")
    public String searchPitcher(@RequestParam(value = "keyword", required = false) String keyword,
                                Model model) {

        List<PitcherSearchDTO> result;
        //이름이 널이 아니아고 공백이 아니라면
        if (keyword != null && !keyword.isBlank()) {
            result = pitcherService.searchPitchers(keyword);
        } else {
            result = List.of(); //빈리스트 반환
        }

        //최신 기록 가져오기
        LocalDate latestDate = pitcherStatsRepository.findLatestRecordDate();

        model.addAttribute("name", keyword);
        model.addAttribute("pitcherList", result);
        model.addAttribute("latestDate", latestDate);

        return "recordroom/allpitcher";
    }
}
