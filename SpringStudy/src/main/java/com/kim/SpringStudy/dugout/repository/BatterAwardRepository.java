package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatterAwardRepository extends JpaRepository<KBOplayerInfo, Long> {

    // [최적화] 홈런왕: 서브쿼리 JOIN 방식
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.hr DESC), k.name_kr, k.team, k.position_sub, b.hr
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest ON b.player_id = latest.player_id AND b.record_date = latest.max_date
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getHR();

    // [최적화] 타점왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.rbi DESC), k.name_kr, k.team, k.position_sub, b.rbi
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest ON b.player_id = latest.player_id AND b.record_date = latest.max_date
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getRBI();

    // [최적화] 안타왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.h DESC), k.name_kr, k.team, k.position_sub, b.h
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest ON b.player_id = latest.player_id AND b.record_date = latest.max_date
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getH();

    // [최적화] 득점왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.r DESC), k.name_kr, k.team, k.position_sub, b.r
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest ON b.player_id = latest.player_id AND b.record_date = latest.max_date
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getR();

    // [최적화] 타율왕 (팀 경기수 매칭 포함)
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.avg DESC), k.name_kr, k.team, k.position_sub, b.avg
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        -- 선수별 최신 기록 매칭
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest_b ON b.player_id = latest_b.player_id AND b.record_date = latest_b.max_date
        -- 팀별 최신 경기수 매칭
        JOIN (
            SELECT team_name, MAX(record_date) as max_date
            FROM kbo
            GROUP BY team_name
        ) latest_g ON g.team_name = latest_g.team_name AND g.record_date = latest_g.max_date
        WHERE b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getAVG();

    // [최적화] 출루율왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.obp DESC), k.name_kr, k.team, k.position_sub, b.obp
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest_b ON b.player_id = latest_b.player_id AND b.record_date = latest_b.max_date
        JOIN (
            SELECT team_name, MAX(record_date) as max_date
            FROM kbo
            GROUP BY team_name
        ) latest_g ON g.team_name = latest_g.team_name AND g.record_date = latest_g.max_date
        WHERE b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getOBP();

    // [최적화] 장타율왕
    @Query(value = """
        SELECT DENSE_RANK() OVER (ORDER BY b.slg DESC), k.name_kr, k.team, k.position_sub, b.slg
        FROM batter_stats b
        JOIN kboplayer_info k ON b.player_id = k.id
        JOIN kbo g ON g.team_name = k.team
        JOIN (
            SELECT player_id, MAX(record_date) as max_date
            FROM batter_stats
            GROUP BY player_id
        ) latest_b ON b.player_id = latest_b.player_id AND b.record_date = latest_b.max_date
        JOIN (
            SELECT team_name, MAX(record_date) as max_date
            FROM kbo
            GROUP BY team_name
        ) latest_g ON g.team_name = latest_g.team_name AND g.record_date = latest_g.max_date
        WHERE b.pa >= g.game * 3.1
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getSLG();
}