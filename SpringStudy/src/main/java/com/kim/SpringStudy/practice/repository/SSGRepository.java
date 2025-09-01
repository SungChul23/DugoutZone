package com.kim.SpringStudy.practice.repository;

import com.kim.SpringStudy.practice.domain.SSG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SSGRepository extends JpaRepository<SSG,String> {

    @Query(value = "SELECT number, player, position, years FROM ssg WHERE MATCH(player) AGAINST(?1)", nativeQuery = true)
    List<SSG> rawQuery(String player);

}
