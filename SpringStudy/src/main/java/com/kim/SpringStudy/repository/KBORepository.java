package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface KBORepository extends JpaRepository<KBO,String> {
    List<KBO> findByRecordDateOrderByRankNumAsc(LocalDate recordDate);
    @Query("SELECT DISTINCT k.recordDate FROM KBO k ORDER BY k.recordDate DESC")
    List<LocalDate> findAllRecordDates();

}
