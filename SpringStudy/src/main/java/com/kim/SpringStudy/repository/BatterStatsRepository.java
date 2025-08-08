package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.BatterStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatterStatsRepository extends JpaRepository<BatterStats,Long> {
}
