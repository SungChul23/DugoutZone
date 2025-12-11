package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.BatterStats;
import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.kim.SpringStudy.dugout.dto.BatterSearchDTO;

import java.time.LocalDate;
import java.util.List;

public interface BatterStatsRepository extends JpaRepository<BatterStats, Long> {

    boolean existsByPlayerAndRecordDate(KBOplayerInfo player, LocalDate recordDate);

    // 최신 날짜 구하기
    @Query("SELECT MAX(b.recordDate) FROM BatterStats b WHERE LOWER(b.player.team) = LOWER(:team)")
    LocalDate findLatestDateByTeam(@Param("team") String team);

    // 최신 날짜 + 팀별 조회 (정렬은 Sort로)
    List<BatterStats> findByPlayer_TeamIgnoreCaseAndRecordDate(String team, LocalDate latestDate, Sort sort);

    // 최신 날짜 + 팀별 + 선수 이름 검색
    List<BatterStats> findByPlayer_TeamIgnoreCaseAndRecordDateAndPlayer_NameKrContaining(
            String team,
            LocalDate latestDate,
            String nameKr,
            Sort sort
    );

    //kbo 기록실 - 모든 타자 검색 (DTO 생성함)
// kbo 기록실 - 모든 타자 검색 (선수별 최신 기록 기준)
    @Query("SELECT new com.kim.SpringStudy.dugout.dto.BatterSearchDTO(" +
            "p.nameKr, p.team, b.avg, b.g, b.pa, b.ab, b.r, b.h, b.twoB, " +
            "b.threeB, b.hr, b.tb, b.rbi, b.sac, b.sf, b.bb, b.ibb, b.hbp, " +
            "b.so, b.gdp, b.slg, b.obp, b.ops, b.mh, b.risp, b.phBa) " +
            "FROM BatterStats b " +
            "JOIN b.player p " +  // BatterStats를 기준으로 Player를 조인하는 것이 더 자연스럽습니다
            "WHERE b.recordDate = (" +
            "    SELECT MAX(b2.recordDate) " +
            "    FROM BatterStats b2 " +
            "    WHERE b2.player.id = p.id" +  // [핵심] '이 선수(p.id)'의 기록 중 가장 최신 날짜
            ") " +
            "AND p.nameKr LIKE %:keyword%")
    List<BatterSearchDTO> searchByName(@Param("keyword") String keyword);


    @Query("SELECT MAX(b.recordDate) FROM BatterStats b")
    LocalDate findLatestRecordDate();

}
