package com.kim.SpringStudy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> { //아이템 테이블에서의 기본키 타입

    List<Item> findAllByTitleContains(String title);

    //full text index는 따로 JPA가 없어 쿼리문 생성
    // ? -> 파라미터 생성 한뒤 곧바로 함수 생성
    @Query(value = "select * from item where match(title) against(?1)", nativeQuery = true)
    List<Item> rawQuery(String text);

}
