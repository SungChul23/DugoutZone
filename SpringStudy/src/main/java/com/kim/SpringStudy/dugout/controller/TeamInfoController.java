package com.kim.SpringStudy.dugout.controller;

import com.kim.SpringStudy.dugout.domain.*;
import com.kim.SpringStudy.dugout.repository.KBORepository;
import com.kim.SpringStudy.dugout.repository.KBOTeamRepository;
import com.kim.SpringStudy.dugout.repository.KBOplayerInfoRepository;
import com.kim.SpringStudy.dugout.service.BatterService;
import com.kim.SpringStudy.dugout.service.PitcherService;
import com.kim.SpringStudy.dugout.util.TeamNameMapper;
import com.kim.SpringStudy.dugout.util.TeamSloganUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
//각 팀별 사이트에 있어야 하는 객체에 관한 컨트롤러
public class TeamInfoController {

    private final KBOTeamRepository kboTeamRepository;
    private final KBORepository kboRepository;
    private final KBOplayerInfoRepository kbOplayerInfoRepository;
    private final BatterService batterService;
    private final PitcherService pitcherService;

    //각 팀 사이트 배너에 객체들
    @GetMapping("/team/{teamName}")
    public String teamPage(@PathVariable String teamName, Model model) {
        String viewFolder = TeamNameMapper.toViewFolder(teamName);
        if (viewFolder == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (viewFolder): " + teamName);
        }

        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
        if (dbTeamName == null) {
            throw new IllegalArgumentException("지원하지 않는 팀명 (DB 조회용): " + teamName);
        }

        Optional<KBO> record = kboRepository.findLatestByTeamName(dbTeamName);
        if (record.isEmpty()) {
            throw new IllegalStateException("기록이 존재하지 않습니다: " + teamName);
        }

        String ticketUrl = kboTeamRepository.findTicketUrlByCss(viewFolder).orElse(null);
        String homePageUrl = kboTeamRepository.findHomePageUrlByCss(viewFolder).orElse(null);
        String teamFullName = kboTeamRepository.findNameByCss(viewFolder).orElse(null);

        model.addAttribute("record", record.get()); // 배너에 각 팀 성적 전달
        model.addAttribute("teamName", viewFolder); //url전달
        model.addAttribute("ticketUrl", ticketUrl); //티켓예매링크 전달
        model.addAttribute("homePageUrl", homePageUrl); //공식 홈피 전달
        model.addAttribute("teamFullName", teamFullName); //각 구단 한글 풀네임

        return "teams/" + viewFolder;
    }

    // 구단별 선수진
    @GetMapping("/team/{teamName}/player")
    public String player(@PathVariable String teamName,
                         @RequestParam(defaultValue = "투수") String position,
                         @RequestParam(required = false) String keyword,
                         Model model) {

        String dbTeamName = TeamNameMapper.toDbTeamName(teamName);
        if (dbTeamName == null) {
            throw new IllegalArgumentException("DB 팀명 매핑 실패: " + teamName);
        }

        List<KBOplayerInfo> result;


        if (keyword != null && !keyword.isBlank()) {
            result = kbOplayerInfoRepository.findByTeamAndNameKrContaining(dbTeamName, keyword);
        } else {
            result = kbOplayerInfoRepository.findByTeamAndPosition(dbTeamName, position);
        }

        model.addAttribute("players", result);
        model.addAttribute("teamName", teamName); // ← URL용 원본
        model.addAttribute("position", position);
        model.addAttribute("keyword", keyword);
        model.addAttribute("teamSlogan", TeamSloganUtil.getSlogan(teamName));// 팀슬로건

        return "teams/playerView";
    }


    //각 팀 마다 타자 기록 보여주기
    //team 이름으로 경로 찾고 타율을 기본값으로. 내림차순으로 기본값으로. "" -> 전체 표시
    @GetMapping("/team/{team}/batter")
    public String teamBatters(@PathVariable String team,
                              @RequestParam(defaultValue = "1") int view,
                              @RequestParam(defaultValue = "avg") String sort,   // 기본: 타율
                              @RequestParam(defaultValue = "desc") String order, // 기본: 내림차순
                              @RequestParam(defaultValue = "") String q,
                              Model model) {

        //css 전달용 (팀마다 전용 css)
        String teamCode = TeamNameMapper.toViewFolder(team);

        String dbTeamName = TeamNameMapper.toDbTeamName(team);

        String teamName = dbTeamName;

        String viewFolder = TeamNameMapper.toViewFolder(teamName);
        String teamFullName = kboTeamRepository.findNameByCss(viewFolder).orElse(null);
        model.addAttribute("teamFullName", teamFullName); //각 구단 한글 풀네임

        //슬로건 (유틸이 내부에서 대문자 키로 처리)


        //db 팀 조회
        List<BatterStats> rows = batterService.listByTeam(dbTeamName, q, sort, order);
        // 뷰 전송
        model.addAttribute("teamCode", teamCode);     // CSS/테마용: <body data-team="${teamCode}">
        model.addAttribute("teamName", teamName);     // 페이지 타이틀/배너용
        model.addAttribute("teamSlogan", TeamSloganUtil.getSlogan(team));// 팀슬로건 // 배너 슬로건


        // 상단 폼 유지값
        model.addAttribute("view", view);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("q", q);

        //최신날짜
        model.addAttribute("latestDate", batterService.getLatestDate(dbTeamName));
        model.addAttribute("batters", rows);


        return "teams/batterList";
    }

    //각 팀 마다 투수 기록 보여주기
    @GetMapping("/team/{team}/pitcher")
    public String teamPitcher(@PathVariable String team,
                              @RequestParam(defaultValue = "1") int view,
                              @RequestParam(defaultValue = "era") String sort,
                              @RequestParam(defaultValue = "asc") String order,
                              @RequestParam(defaultValue = "") String q,
                              Model model) {

        //css 전달용 (팀마다 전용 css)
        String teamCode = TeamNameMapper.toViewFolder(team);
        String dbTeamName = TeamNameMapper.toDbTeamName(team);
        String teamName = dbTeamName;

        //구단 풀네임(한) 전달
        String viewFolder = TeamNameMapper.toViewFolder(teamName);
        String teamFullName = kboTeamRepository.findNameByCss(viewFolder).orElse(null);
        model.addAttribute("teamFullName", teamFullName); //각 구단 한글 풀네임

        // 데이터 조회
        List<PitcherStats> rows = pitcherService.listByTeam(dbTeamName, q, sort, order);

        //뷰 바인딩
        model.addAttribute("teamCode", teamCode);     // CSS/테마용: <body data-team="${teamCode}">
        model.addAttribute("teamName", teamName);     // 페이지 타이틀/배너용
        model.addAttribute("teamSlogan", TeamSloganUtil.getSlogan(team));// 팀슬로건

        //배너 아래 검색 폼
        model.addAttribute("view", view);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        //최신날짜
        model.addAttribute("latestDate", pitcherService.getLatestDate(dbTeamName));
        model.addAttribute("pitchers", rows);

        return "teams/pitcherList";

    }

    //구단 별 예매 링크
    @GetMapping("/tickets")
    public String Tickets(Model model) {
        List<KBOTeam> result = kboTeamRepository.findAll();
        model.addAttribute("topTeams", result.subList(0, 5));
        model.addAttribute("bottomTeams", result.subList(5, 10));
        return "games/tickets";
    }
}
