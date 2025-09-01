package com.kim.SpringStudy.practice.controller;

import com.kim.SpringStudy.practice.domain.Comment;
import com.kim.SpringStudy.practice.domain.Item;
import com.kim.SpringStudy.practice.repository.CommentRepository;
import com.kim.SpringStudy.practice.repository.ItemRepository;
import com.kim.SpringStudy.practice.repository.UserRepository;
import com.kim.SpringStudy.practice.domain.Sales;
import com.kim.SpringStudy.practice.repository.SalesRepository;
import com.kim.SpringStudy.practice.service.ItemService;
import com.kim.SpringStudy.dugout.service.S3Service;

import com.kim.SpringStudy.practice.service.SalesService;
import com.kim.SpringStudy.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor //롬복 문법

//inal이나 @NonNull이 붙은 필드들만 포함한 생성자를 자동으로 생성
public class ItemController {
    //오브젝트 뽑아서  넣어주세요.(레포지, 서비스)
    private final ItemRepository itemRepository;
    private final ItemService itemService; //서비스 폴더내 saveItem함수를 사용하기 위한 변수 설정
    private final S3Service s3Service;
    private final CommentRepository commentRepository;
    private final SalesRepository salesRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SalesService salesService;

//    @Autowired (롬복 미사용 시)
//    public ItemController(ItemRepository itemRepository, ItemService itemService) {
//        this.itemRepository = itemRepository;
//        this.itemService = itemService;
//    }

    @GetMapping("/list")
    String list(Model model) {
        List<Item> result = itemService.findItem(); // 디펜던시 인잭션으로 findItem 함수 가져옴
        model.addAttribute("items", result);

        return "springStudy/list";
    }

    //상세페이지 + 댓글
    @GetMapping("/list/product/{id}")
    String product(@PathVariable long id, Model model) {

        try {
            Optional<Item> product = itemRepository.findById(id);
            if (product.isPresent()) { //값이 있나요?
                model.addAttribute("product", product.get()); //optional 타입

                List<Comment> comments = commentRepository.findByParentId(id);
                model.addAttribute("comments", comments);
                return "springStudy/product";
            } else {
                return "springStudy/list";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "springStudy/list";
        }


    }

    @GetMapping("/write")
    String write() {
        return "springStudy/write.html";
    }


    @PostMapping("/add")
    String addpost(@RequestParam String title,
                   @RequestParam Integer price,
                   @RequestParam String imageUrl) {
        //오브젝 뽑아쓴걸 사용 = 디펜던시 인젝션 패턴
        itemService.saveItem(title, price, imageUrl);
        System.out.println("받은 imageUrl = " + imageUrl);

        return "springStudy/list";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id); // 디펜던시 인잭션으로 findItem 함수 가져옴
        if (result.isPresent()) {
            model.addAttribute("items", result.get());
            return "springStudy/edit";
        }
        return "springStudy/list";
    }

    @PostMapping("/edit")
    public String editpost(@RequestParam Long id,
                           @RequestParam String title,
                           @RequestParam Integer price,
                           @RequestParam String imageUrl) {
        itemService.editItem(id, title, price, imageUrl);
        return "springStudy/list";
    }

    @GetMapping("/delete/{id}")
    String delete(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id); // 디펜던시 인잭션으로 findItem 함수 가져옴
        if (result.isPresent()) {
            model.addAttribute("items", result.get());
            return "springStudy/delete";
        }
        return "springStudy/list";
    }

    @PostMapping("/delete")
    public String deletepost(@RequestParam Long id) {
        itemService.deleteItem(id);
        return "springStudy/list";
    }


    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        var result = s3Service.createPresignedUrl("test/" + filename);
        System.out.println(result);
        return result;
    }

    //상품 검색
    @PostMapping("/search")
    public String searchItem(@RequestParam String title, Model model) {
        var result = itemRepository.rawQuery(title);
        model.addAttribute("items", result);
        model.addAttribute("emptyResult", result == null || result.isEmpty());
        System.out.println("출력문: " + result);

        return "springStudy/list";
    }

    //상품 주문
    @PostMapping("/order")
    public String orderItem(@RequestParam String title,
                            @RequestParam Integer price,
                            @RequestParam Integer count,
                            @RequestParam long id
    ) {
        salesService.orderItem(title, price, count , id);
        return "springStudy/myOrders";
    }

    @GetMapping("/myOrders")
    public String orderAll(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login?message=needLogin";
        }
        String username = principal.getName();
        List<Sales> myOrder = salesService.getMyOrders(username);
        System.out.println("내 주문 내역" + myOrder);
        model.addAttribute("orders", myOrder);
        return "springStudy/myOrders";
    }

}


//1. 레포지 만들기 2. 원하는 클래스에 레포지 등록, 3.레포지.입출력분법 작성
//1. 레포지에서 인터페이스 생성후 테이블명, 기본키 타입작성
//2. 아이템 컨트롤러 들어가서 레포지에 대한 변수 설정 (private 변수명)
//2.1 @RequiredArgsConstructor 사용 (롬복 사용) -> 그렇지 않으면 @AutoWried로 주입해야함
//3. 아이템 컨트롤러에 생성한 레포지 변수를 통하여 함수 사용 ex) findall(), get.(0).title ~ 등등

// --- ajax ---
//form 말고 ajax로 데이터전송하면 @RequestBody를 사용
// 클라이언트와 통신시 서버 에러 코드도 보내주기()
//return ResponseEntity.status(Httpstatus.NOT_FOUND).body()