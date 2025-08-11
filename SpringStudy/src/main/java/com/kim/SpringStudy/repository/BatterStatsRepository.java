package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.BatterStats;
import com.kim.SpringStudy.domain.KBOplayerInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
