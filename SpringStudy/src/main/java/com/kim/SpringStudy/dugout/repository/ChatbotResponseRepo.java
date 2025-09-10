package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ChatbotResponseRepo extends JpaRepository<KBOplayerInfo, Long> {

    //출생년도 조회
    @Query(value = """
            SELECT k.name_kr AS nameKr, k.team, k.position, k.birth, k.image_url AS imageUrl
            FROM kboplayer_info k
            WHERE k.birth LIKE CONCAT(:year, '%')
            AND (:team IS NULL OR k.team = :team)
            AND (:position IS NULL OR k.position = :position)
            ORDER BY k.team, k.name_kr
            LIMIT 50
            """, nativeQuery = true)
    List<Map<String, Object>> findBornPlayers(@Param("year") String year,
                                              @Param("team") String team,
                                              @Param("position") String position);


    //조견형 기록 조회 (타자 전용)
    @Query(value = """
            SELECT p.name_kr AS nameKr, p.team, p.position,
                   b.hr, b.avg, b.rbi, b.so, b.h, b.r,
                   b.obp, b.slg, b.ops, b.gdp
            FROM kboplayer_info p
            JOIN batter_stats b 
              ON p.id = b.player_id
            WHERE b.record_date = (SELECT MAX(record_date) FROM batter_stats)
              AND (:team IS NULL OR p.team = :team)
              AND (
                  (:statType = 'HR'   AND b.hr   >= :value) OR
                  (:statType = 'AVG'  AND b.avg  >= :value) OR
                  (:statType = 'RBI'  AND b.rbi  >= :value) OR
                  (:statType = 'SO'   AND b.so   >= :value) OR
                  (:statType = 'H'    AND b.h    >= :value) OR
                  (:statType = 'R'    AND b.r    >= :value) OR
                  (:statType = 'OBP'  AND b.obp  >= :value) OR
                  (:statType = 'SLG'  AND b.slg  >= :value) OR
                  (:statType = 'OPS'  AND b.ops  >= :value) OR
                  (:statType = 'GDP'  AND b.gdp  >= :value)
              )
            ORDER BY p.team, p.name_kr
            LIMIT 50
            """, nativeQuery = true)
    List<Map<String, Object>> findBatterByCondition(@Param("team") String team,
                                                    @Param("statType") String statType,
                                                    @Param("value") double value);

    //조견형 기록 조회 (투수 전용)
    @Query(value = """
            SELECT p.name_kr AS nameKr, p.team, p.position,
                   pit.w    AS w,
                   pit.l    AS l,
                   pit.sv   AS sv,
                   pit.hld  AS hld,
                   pit.ip   AS ip,
                   pit.so   AS so,
                   pit.era  AS era,
                   pit.wpct AS wpct
            FROM kboplayer_info p
            JOIN pitcher_stats pit 
              ON p.id = pit.player_id
            WHERE pit.record_date = (SELECT MAX(record_date) FROM pitcher_stats)
              AND (:team IS NULL OR p.team = :team)
              AND (
                  (:statType = 'W'     AND pit.w     >= :value) OR
                  (:statType = 'L'     AND pit.l     >= :value) OR
                  (:statType = 'SV'    AND pit.sv    >= :value) OR
                  (:statType = 'HLD'   AND pit.hld   >= :value) OR
                  (:statType = 'IP'    AND pit.ip    >= :value) OR
                  (:statType = 'SO'    AND pit.so    >= :value) OR
                  (:statType = 'ERA'   AND pit.era   <= :value) OR
                  (:statType = 'WPCT'  AND pit.wpct  >= :value)
              )
            ORDER BY p.team, p.name_kr
            LIMIT 50
            """, nativeQuery = true)
    List<Map<String, Object>> findPitcherByCondition(@Param("team") String team,
                                                     @Param("statType") String statType,
                                                     @Param("value") double value);

}
