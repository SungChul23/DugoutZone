package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.BatterStats;
import com.kim.SpringStudy.domain.KBOplayerInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BatterStatsRepository extends JpaRepository<BatterStats, Long> {

    boolean existsByPlayerAndRecordDate(KBOplayerInfo player, LocalDate recordDate);
    //Optional<BatterStats> findByPlayerAndRecordDate(KBOplayerInfo player, LocalDate recordDate); // 업서트용

    //팀 찾기
    List<BatterStats> findByPlayer_TeamIgnoreCase(String team, Sort sort);

    //팀 + 이름
    List<BatterStats> findByPlayer_TeamIgnoreCaseAndPlayer_NameKrContaining
    (String team, String nameKr, Sort sort);


}
