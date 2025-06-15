package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.domain.Item;
import com.kim.SpringStudy.domain.ItemRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
//롬복 문법
//inal이나 @NonNull이 붙은 필드들만 포함한 생성자를 자동으로 생성
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String list(Model model) {
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);

        return "list";
    }

    @GetMapping("/list/product/{id}")
    String product(@PathVariable long id, Model model) {
        Optional<Item> product = itemRepository.findById(id);
        if (product.isPresent()) { //값이 있나요?
            model.addAttribute("product",product.get()); //optional 타입
            System.out.println(product.get());
        }else {
            return "redirect:/list";
        }


        return "product";
    }

    @GetMapping("/write")
    String write() {
        return "write.html";
    }
    @PostMapping("/add")
    String addpost(String title, Integer price){


        if(title == null || title.trim().isEmpty() || price == null ){
            return "error";
        }
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);


        return "redirect:/list";
    }

}








//1. 레포지 만들기 2. 원하는 클래스에 레포지 등록, 3.레포지.입출력분법 작성
//1. 레포지에서 인터페이스 생성후 테이블명, 기본키 타입작성
//2. 아이템 컨트롤러 들어가서 레포지에 대한 변수 설정 (private 변수명)
//2.1 @RequiredArgsConstructor 사용 (롬복 사용) -> 그렇지 않으면 @AutoWried로 주입해야함
//3. 아이템 컨트롤러에 생성한 레포지 변수를 통하여 함수 사용 ex) findall(), get.(0).title ~ 등등

//form 말고 ajax로 데이터전송하면 @RequestBody를 사용