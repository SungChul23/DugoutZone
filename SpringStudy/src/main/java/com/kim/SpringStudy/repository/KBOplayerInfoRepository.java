package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KBOplayerInfoRepository extends JpaRepository<KBOplayerInfo,Long> {


    // 포지션으로 필터
    List<KBOplayerInfo> findByTeamAndPosition(String team, String position);

    // 이름으로 검색 (포지션 무시)
    List<KBOplayerInfo> findByTeamAndNameKrContaining(String team, String keyword);
    //타자 , 투수 db넣기 위한 메서드
    Optional<KBOplayerInfo> findByNameKr(String nameKr);
}
