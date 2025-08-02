package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBOGameDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KBOGameDateRepository extends JpaRepository<KBOGameDate, Long> {


    //특정 날짜 경기 조회
    List<KBOGameDate> findByGameDate(String gameDate);


    //중복 제거된 모든 경기 날짜 조회 (오름차순 정렬)
    @Query("SELECT DISTINCT g.gameDate FROM KBOGameDate g ORDER BY g.gameDate ASC")
    List<String> findDistinctGameDates();


    //가장 최신 경기 날짜 조회 (하나만)
    @Query("SELECT g.gameDate FROM KBOGameDate g ORDER BY g.gameDate DESC LIMIT 1")
    String findLatestGameDate();
}
