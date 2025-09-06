package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ChatbotResponseRepo extends JpaRepository<KBOplayerInfo, Long> {

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


}
