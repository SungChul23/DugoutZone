package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PitcherAwardRepository extends JpaRepository<KBOplayerInfo, Long> {

    //다승왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.w
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        ORDER BY p.w DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getW();
    //삼진왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.so
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        ORDER BY p.so DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getSO();
    //세이브왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.sv
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        ORDER BY p.sv DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getSV();
    //홀드왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.hld
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        ORDER BY p.hld DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getHLD();
    //이닝왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.ip
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        ORDER BY 
            CAST(REGEXP_SUBSTR(p.ip, '^[0-9]+') AS DECIMAL(6,3)) + 
            CASE 
                WHEN p.ip LIKE '%1/3' THEN 0.333
                WHEN p.ip LIKE '%2/3' THEN 0.667
                ELSE 0 
            END DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getIP();
    //평균 자책점 왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.era
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        AND (
            CAST(REGEXP_SUBSTR(p.ip, '^[0-9]+') AS DECIMAL(6,3)) + 
            CASE 
                WHEN p.ip LIKE '%1/3' THEN 0.333
                WHEN p.ip LIKE '%2/3' THEN 0.667
                ELSE 0 
            END
        ) >= 144
        ORDER BY p.era ASC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getERA();
    //승률왕
    @Query(value = """
        SELECT k.name_kr, k.team, k.position, p.wpct
        FROM pitcher_stats p
        JOIN kboplayer_info k ON p.player_id = k.id
        WHERE p.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
        AND (
            CAST(REGEXP_SUBSTR(p.ip, '^[0-9]+') AS DECIMAL(6,3)) + 
            CASE 
                WHEN p.ip LIKE '%1/3' THEN 0.333
                WHEN p.ip LIKE '%2/3' THEN 0.667
                ELSE 0 
            END
        ) >= 144
        ORDER BY p.wpct DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getWPCT();
}
