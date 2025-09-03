package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface GoldenGloveRepository extends JpaRepository<KBOplayerInfo, Long>  {

    // 내야수, 포수, 지명타자 position_sub 기준
    @Query(value = """
        SELECT
            p.name_kr,
            p.team,
            p.position_sub,
            CAST((
                SUM(s.obp) * 25 + SUM(s.slg) * 25 + SUM(s.hr) * 2 + SUM(s.rbi) * 1.2 +
                SUM(s.avg) * 100 + SUM(s.risp) * 15 + SUM(s.mh) * 0.5 -
                SUM(s.so) * 0.1 - SUM(s.gdp) * 0.1
            ) AS SIGNED) AS golden_glove_score
        FROM batter_stats s
        JOIN kboplayer_info p ON s.player_id = p.id
        JOIN kbo k ON p.team = k.team_name
        WHERE p.position_sub = :positionSub
          AND k.record_date = (SELECT MAX(record_date) FROM kbo)
        GROUP BY p.name_kr, p.team, p.position_sub
        HAVING SUM(s.ab) >= (MAX(k.game) * 3.1)
        ORDER BY golden_glove_score DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> topPlayersByPositionSub(@Param("positionSub") String positionSub,
                                           @Param("limit") int limit);

    // 외야수 부분
    @Query(value = """
        SELECT
            p.name_kr,
            p.team,
            p.position_sub,
            CAST((
                SUM(s.obp) * 25 + SUM(s.slg) * 25 + SUM(s.hr) * 2 + SUM(s.rbi) * 1.2 +
                SUM(s.avg) * 100 + SUM(s.risp) * 15 + SUM(s.mh) * 0.5 -
                SUM(s.so) * 0.1 - SUM(s.gdp) * 0.1
            ) AS SIGNED) AS golden_glove_score
        FROM batter_stats s
        JOIN kboplayer_info p ON s.player_id = p.id
        JOIN kbo k ON p.team = k.team_name
        WHERE p.position_sub IN ('좌익수', '중견수', '우익수')
          AND k.record_date = (SELECT MAX(record_date) FROM kbo)
        GROUP BY p.name_kr, p.team, p.position_sub
        HAVING SUM(s.ab) >= (MAX(k.game) * 3.1)
        ORDER BY golden_glove_score DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> topOutfielders(@Param("limit") int limit);

    // 투수 부분
    @Query(value = """
        SELECT
            p.name_kr,
            p.team,
            p.position,
            CAST((
                SUM(s.w) * 3 + SUM(s.sv) * 1.5 + SUM(s.hld) * 1.5 +
                SUM(s.so) * 0.1 + SUM(s.qs) * 2 -
                SUM(s.era) * 20 - SUM(s.whip) * 10 +
                (SUM(s.ip) / COUNT(s.id)) * 0.5 -
                SUM(s.hr) * 0.5 - SUM(s.bb) * 0.1 - SUM(s.r) * 0.1
            ) AS SIGNED) AS golden_glove_score
        FROM pitcher_stats s
        JOIN kboplayer_info p ON s.player_id = p.id
        JOIN kbo k ON p.team = k.team_name
        WHERE p.position = '투수'
          AND k.record_date = (SELECT MAX(record_date) FROM kbo)
        GROUP BY p.name_kr, p.team, p.position
        HAVING SUM(s.ip) / COUNT(s.id) >= MAX(k.game)
        ORDER BY golden_glove_score DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> topPitchers(@Param("limit") int limit);


    // 포수 부분
    @Query(value = """
        SELECT
            p.name_kr,
            p.team,
            p.position,
            CAST((
                SUM(s.obp) * 25 + SUM(s.slg) * 25 + SUM(s.hr) * 2 + SUM(s.rbi) * 1.2 +
                SUM(s.avg) * 100 + SUM(s.risp) * 15 + SUM(s.mh) * 0.5 -
                SUM(s.so) * 0.1 - SUM(s.gdp) * 0.1
            ) AS SIGNED) AS golden_glove_score
        FROM batter_stats s
        JOIN kboplayer_info p ON s.player_id = p.id
        JOIN kbo k ON p.team = k.team_name
        WHERE p.position = '포수'
          AND k.record_date = (SELECT MAX(record_date) FROM kbo)
        GROUP BY p.name_kr, p.team, p.position
        HAVING SUM(s.ab) >= (MAX(k.game) * 3.1)
        ORDER BY golden_glove_score DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> topCatchers(@Param("limit") int limit);
}
