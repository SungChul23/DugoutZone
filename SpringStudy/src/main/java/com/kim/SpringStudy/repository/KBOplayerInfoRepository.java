package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KBOplayerInfoRepository extends JpaRepository<KBOplayerInfo,Long> {


    //팀명과 포지션을 찾아 선수단보기에 출력
    List<KBOplayerInfo> findByTeamAndPosition (String name, String position);
}
