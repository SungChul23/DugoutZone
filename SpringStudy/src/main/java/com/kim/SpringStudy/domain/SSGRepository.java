package com.kim.SpringStudy.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SSGRepository extends JpaRepository<SSG,String> {

    @Query(value = "SELECT number, player, position, years FROM ssg WHERE MATCH(player) AGAINST(?1)", nativeQuery = true)
    List<SSG> rawQuery(String player);

}
