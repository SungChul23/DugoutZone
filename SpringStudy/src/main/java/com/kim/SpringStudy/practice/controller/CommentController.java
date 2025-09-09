//package com.kim.SpringStudy.practice.controller;
//
//import com.kim.SpringStudy.practice.domain.Comment;
//import com.kim.SpringStudy.practice.repository.CommentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//
//@Controller
//@RequiredArgsConstructor
//
//public class CommentController {
//
//    private final CommentRepository commentRepository;
//
//    @PostMapping("/comment")
//    public String addComment(@RequestParam Long parentId,
//                             @RequestParam String content,
//                             Principal principal) {
//
//        if(principal == null){
//            return "redirect:/login?message=needLogin";
//        }
//
//        String username = principal.getName(); // 사용자 이름만 얻기 가능
//
//        var data = new Comment();
//        data.setContent(content);
//        data.setUsername(username);
//        data.setParentId(parentId);
//
//        commentRepository.save(data);
//        return "redirect:/list/product/" + parentId;
//    }
//}
