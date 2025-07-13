package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.domain.Comment;
import com.kim.SpringStudy.repository.CommentRepository;
import com.kim.SpringStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor

public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @PostMapping("/comment")
    public String addComment(@RequestParam Long parentId,
                             @RequestParam String content,
                             Principal principal) {

        if(principal == null){
            return "redirect:/login?message=needLogin";
        }

        String username = principal.getName(); // 사용자 이름만 얻기 가능

        var data = new Comment();
        data.setContent(content);
        data.setUsername(username);
        data.setParentId(parentId);

        commentRepository.save(data);
        return "redirect:/list/product/" + parentId;
    }
}
