package com.kim.SpringStudy.dugout.repository;

import com.kim.SpringStudy.dugout.domain.KBOTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KBOTeamRepository extends JpaRepository<KBOTeam,Long> {

    //css 칼럼으로 티켓링크 url 가져오기
    @Query("select t.ticketUrl from KBOTeam t where lower(t.cssClass) = lower(:css)")
    Optional<String> findTicketUrlByCss(@Param("css") String css);

    //css 칼럼으로 구단 정식 홈페이지  url 가져오기
    @Query("select t.homePageUrl from KBOTeam t where lower(t.cssClass) = lower(:css)")
    Optional<String> findHomePageUrlByCss(@Param("css") String css);

    @Query("select t.name from KBOTeam t where t.cssClass = :css")
    Optional<String> findNameByCss(@Param("css") String css);
}
