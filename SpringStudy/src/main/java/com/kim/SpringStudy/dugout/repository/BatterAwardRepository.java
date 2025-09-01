package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatterAwardRepository extends JpaRepository<KBOplayerInfo, Long> {

    // 홈런왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.hr DESC), k.name_kr, k.team, k.position_sub, b.hr
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getHR();

    // 타점왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.rbi DESC), k.name_kr, k.team, k.position_sub, b.rbi
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getRBI();

    // 안타왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.h DESC), k.name_kr, k.team, k.position_sub, b.h
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getH();

    // 득점왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.r DESC), k.name_kr, k.team, k.position_sub, b.r
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getR();

    // 타율왕 (규정 타석 기준)
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.avg DESC), k.name_kr, k.team, k.position_sub, b.avg
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
          AND g.record_date = (SELECT MAX(record_date) FROM kbo)
          AND b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getAVG();

    // 출루율왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.obp DESC), k.name_kr, k.team, k.position_sub, b.obp
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
          AND g.record_date = (SELECT MAX(record_date) FROM kbo)
          AND b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getOBP();

    // 장타율왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.slg DESC), k.name_kr, k.team, k.position_sub, b.slg
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
          AND g.record_date = (SELECT MAX(record_date) FROM kbo)
          AND b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getSLG();
}
