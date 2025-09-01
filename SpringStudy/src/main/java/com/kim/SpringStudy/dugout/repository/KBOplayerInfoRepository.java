package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOplayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface KBOplayerInfoRepository extends JpaRepository<KBOplayerInfo, Long> {


    // 포지션으로 필터
    List<KBOplayerInfo> findByTeamAndPosition(String team, String position);

    // 이름으로 검색 (포지션 무시)
    List<KBOplayerInfo> findByTeamAndNameKrContaining(String team, String keyword);

    // 이름+팀으로 전부 (안전망)
    List<KBOplayerInfo> findAllByNameKrAndTeam(String nameKr, String team);

    // 배터 포지션 우선 매칭
    Optional<KBOplayerInfo> findFirstByNameKrAndTeamAndPositionIn(
            String nameKr, String team, Collection<String> positions);

    // 이름과 팀으로 단일 선수 검색
    KBOplayerInfo findByNameKrAndTeam(String nameKr, String team);

    KBOplayerInfo findByNameKrAndTeamAndBirth(String nameKR, String team, String birth);


    //동명이인 방지 id까지 같이 수집
    KBOplayerInfo findByTeamAndKboPlayerId(String team, String kboPlayerId);
    List<KBOplayerInfo> findAllByTeamAndNameKr(String team, String nameKr);

    //포지션 구체화 수집
    List<KBOplayerInfo> findAllByNameKr(String nameKr);





}
