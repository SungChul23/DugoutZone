package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,String>{
    boolean existsByUsername(String username); // 아이디 중복검사

    Optional<User> findByUsername(String username); //아이디 찾아주세요

    //SELECT * FROM user WHERE username = ? 와 같은 SQL을 자동 생성

}
