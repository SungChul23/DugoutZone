package com.kim.SpringStudy.controller;

import com.kim.SpringStudy.domain.User;
import com.kim.SpringStudy.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository; //유저 레포지 객체 생성
    private final PasswordEncoder passwordEncoder; //시큐리티에서 만든 비번인코더 객체(bean)



    @GetMapping("/register")
    String ShowRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String displayName) {
        User user = new User();
        user.setUsername(username);
        var hash = passwordEncoder.encode(password); //시큐리티 passwordEncoder 사용
        user.setPassword(hash); //비번 해싱
        user.setDisplayName(displayName);
        userRepository.save(user);

        return "redirect:/";
    }

    @PostMapping("/distinct")
    @ResponseBody
    public Map<String, Boolean> distinct(@RequestBody Map<String, String> data) {
        // 클라이언트가 보낸 JSON 요청 바디를 Map<String, String>으로 받아온다 (ex: { "username": "kim123" })
        // 'username' 키에 해당하는 값을 추출한다
        String username = data.get("username");
        // UserRepository에 접근하여 해당 username이 DB에 존재하는지 확인한다
        boolean exists = userRepository.existsByUsername(username);
        // 응답용 Map을 만들어 존재 여부를 키 'exists'로 담는다
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/login")
    String ShowLogin(){
        return "login";
    }


    @GetMapping("/user/1")
    @ResponseBody
    public UserDTO getUser(){
        var  a= userRepository.findById("kimsam0923");
        var result = a.get();
        var data = new UserDTO(result.getUsername(), result.getDisplayName());
        return data;
    }
    //DTO
    //DTO를 통해 계층간(Controller ↔ Service ↔ Repository)에 데이터를 전달함
    class UserDTO{
        public String username;
        public String displayName;
        UserDTO(String a, String b){
            this.username = a;
            this.displayName =b;
        }
    }

}
