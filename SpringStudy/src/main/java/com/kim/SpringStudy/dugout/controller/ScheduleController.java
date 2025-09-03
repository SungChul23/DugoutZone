package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.dto.GameDateDTO;
import com.kim.SpringStudy.dugout.service.GameDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.kim.SpringStudy.dugout.util.TeamLogoMapper.teamLogoMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {


    private final GameDateService gameDateService;

    // KBO 2025시즌 일정
    @GetMapping
    public String test(@RequestParam(required = false) String date, Model model) {
        // 모든 날짜 리스트 가져옴
        List<String> availableDates = gameDateService.getAvailableDates();

        // 오늘 날짜 계산 ("08.02" 같은 형식으로)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM.dd"));

        // 오늘 날짜가 DB에 있으면 그것을 사용
        String selectedDate = (date != null) ? date :
                (availableDates.contains(today) ? today :
                        gameDateService.getLatestAvailableDate());

        System.out.println("선택된 날짜 : " + selectedDate);

        // 해당 날짜의 경기 목록 조회
        List<GameDateDTO> games = gameDateService.getGamesByDate(selectedDate);

        // 모델에 전달
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("games", games);

        model.addAttribute("teamLogoMap", teamLogoMap);
        return "games/scheduleView";
    }
}
