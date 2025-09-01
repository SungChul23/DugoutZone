package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.domain.PitcherStats;
import com.kim.SpringStudy.dto.PitcherSearchDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PitcherStatsRepository extends JpaRepository<PitcherStats, Long> {

    @Query("SELECT MAX(p.recordDate) FROM PitcherStats p WHERE LOWER(p.player.team) = LOWER(:team)")
    LocalDate findLatestDateByTeam(@Param("team") String team);

    // ✅ 선수 엔티티(또는 ID) 기준으로 검사하는 편이 안전함
    boolean existsByPlayerAndRecordDate(KBOplayerInfo player, LocalDate recordDate);


    List<PitcherStats> findByPlayer_TeamIgnoreCaseAndRecordDate
            (String team, LocalDate latestDate, Sort sort);

    List<PitcherStats> findByPlayer_TeamIgnoreCaseAndRecordDateAndPlayer_NameKrContaining(
            String team,
            LocalDate latestDate,
            String nameKr,
            Sort sort
    );

    //kbo 기록실 - 모든 투수 검색 (DTO 생성함)
    @Query("""
            SELECT new com.kim.SpringStudy.dto.PitcherSearchDTO(
                p.nameKr, p.team,
                s.era, s.g, s.w, s.l, s.sv, s.hld, s.wpct, s.ip,
                s.h, s.hr, s.bb, s.hbp, s.so, s.r, s.er, s.whip,
                s.cg, s.sho, s.qs, s.bsv, s.tbf, s.np, s.avg,
                s.twoB, s.threeB, s.sac, s.sf, s.ibb, s.wp, s.bk
            )
            FROM KBOplayerInfo p
            JOIN PitcherStats s ON p.id = s.player.id
            WHERE s.recordDate = (
                SELECT MAX(s2.recordDate) FROM PitcherStats s2
            )
            AND p.position = '투수'
            AND p.nameKr LIKE %:keyword%
            """)
    List<PitcherSearchDTO> searchByName(@Param("keyword") String keyword);

    //kbo 기록실 - 최신기록 표시
    @Query("SELECT MAX(b.recordDate) FROM PitcherStats b")
    LocalDate findLatestRecordDate();


}
