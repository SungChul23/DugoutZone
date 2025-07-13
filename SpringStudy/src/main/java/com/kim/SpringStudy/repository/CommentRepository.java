package com.kim.SpringStudy.repository;

import com.kim.SpringStudy.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByParentId(Long a);

}
