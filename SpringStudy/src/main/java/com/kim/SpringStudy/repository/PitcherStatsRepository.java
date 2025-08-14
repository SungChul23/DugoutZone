package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.domain.PitcherStats;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PitcherStatsRepository extends JpaRepository<PitcherStats , Long> {

    @Query("SELECT MAX(p.recordDate) FROM PitcherStats p WHERE LOWER(p.player.team) = LOWER(:team)")
    LocalDate findLatestDateByTeam(@Param("team") String team);

    // ✅ 선수 엔티티(또는 ID) 기준으로 검사하는 편이 안전함
    boolean existsByPlayerAndRecordDate(KBOplayerInfo player, LocalDate recordDate);


    List<PitcherStats> findByPlayer_TeamIgnoreCaseAndRecordDate
            (String team, LocalDate latestDate, Sort sort);

    List<PitcherStats> findByPlayer_TeamIgnoreCaseAndRecordDateAndPlayer_NameKrContaining(
            String team,
            LocalDate latestDate,
            String nameKr,
            Sort sort
    );
}
