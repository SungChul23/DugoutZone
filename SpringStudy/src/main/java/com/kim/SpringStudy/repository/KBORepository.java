package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KBORepository extends JpaRepository<KBO,String> {
    List<KBO> findByRecordDateOrderByRankNumAsc(LocalDate recordDate);
    @Query("SELECT DISTINCT k.recordDate FROM KBO k ORDER BY k.recordDate DESC")
    List<LocalDate> findAllRecordDates();


    //팀 사이트 배너에 삽입되는 데이터들(승무패, 등수, 승률 등등)
    @Query("SELECT k FROM KBO k WHERE k.teamName = :teamName AND k.recordDate = " +
            "(SELECT MAX(k2.recordDate) FROM KBO k2 WHERE k2.teamName = :teamName)")
    Optional<KBO> findLatestByTeamName(@Param("teamName") String teamName);

    // 팀 순위 추세 그래프
    List<KBO> findAllByOrderByRecordDateAscTeamNameAsc();

}
