package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.PitcherStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PitcherStatsRepository extends JpaRepository<PitcherStats , Long> {

    boolean existsByPlayer_IdAndRecordDate(Long playerId, LocalDate recordDate);

    List<PitcherStats> findByPlayer_TeamAndRecordDate(String team, LocalDate recordDate);

    void deleteByPlayer_TeamAndRecordDate(String team, LocalDate recordDate);

    boolean existsByPlayer_NameKrAndPlayer_TeamAndRecordDate(
            String nameKr, String team, LocalDate recordDate
    );
}
