package com.kim.SpringStudy.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SSGRepository extends JpaRepository<SSG,String> {


}
