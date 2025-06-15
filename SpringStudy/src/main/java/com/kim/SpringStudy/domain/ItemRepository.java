package com.kim.SpringStudy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> { //아이템 테이블에서의 기본키 타입

}
