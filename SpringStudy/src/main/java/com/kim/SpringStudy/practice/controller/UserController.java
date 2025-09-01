package com.kim.SpringStudy.practice.controller;

import com.kim.SpringStudy.practice.JWT.JwtUtil;
import com.kim.SpringStudy.practice.domain.User;
import com.kim.SpringStudy.practice.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    //JWT 객체 주입
    private final AuthenticationManagerBuilder authenticationManagerBuilderm;
    private final JwtUtil jwtUtil;


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
    String ShowLogin() {
        return "login";
    }

    @GetMapping("/logout")
    String Logout(HttpServletResponse response) {
        // 쿠키삭제
        Cookie cookie = new Cookie("jwt" , null);
        cookie.setMaxAge(0); //즉시삭제
        cookie.setPath("/"); //모든경로
        response.addCookie(cookie);

        return "redirect:/";
    }


    @GetMapping("/user/1")
    @ResponseBody
    public UserDTO getUser() {
        var a = userRepository.findById("kimsam0923");
        var result = a.get();
        var data = new UserDTO(result.getUsername(), result.getDisplayName());
        return data;
    }

    //DTO
    //DTO를 통해 계층간(Controller ↔ Service ↔ Repository)에 데이터를 전달함
    class UserDTO {
        public String username;
        public String displayName;

        UserDTO(String a, String b) {
            this.username = a;
            this.displayName = b;
        }
    }

    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse httpServletResponse) {

        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"), data.get("password"));
        var auth = authenticationManagerBuilderm.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(jwt);

        // 쿠키에 JWT 저장
        var cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(86400); // 하루
        cookie.setHttpOnly(true); // JS 접근 막음
        cookie.setPath("/"); // 모든 URL에 적용
        httpServletResponse.addCookie(cookie);

        return "OK"; // redirect 대신 OK 반환
    }

    @GetMapping("/my-page/jwt")
    @ResponseBody
    public String myPage(@CookieValue("jwt") String jwt) {
        String username = JwtUtil.getUsernameFromToken(jwt);
        return username + "님의 마이페이지입니다.";
    }

    @GetMapping("/user/info")
    @ResponseBody
    public UserInfoResponse getUserInfo(@CookieValue("jwt") String jwt) {
        String username = JwtUtil.getUsernameFromToken(jwt);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없음"));
        return new UserInfoResponse(user.getUsername(), user.getDisplayName());
    }


    @Data
    @AllArgsConstructor
    public class UserInfoResponse {
        private String username;
        private String displayName;
    }

}

//TIP 유저의 요청은 바로 컨트롤러에 가는 것 보다 필터를 통해 확인하고 간다
// EX)JWT 토큰 확인후 아니면 빠구 ㄱ , 아마 미들웨어랑 비슷한 개념 ㅇㅇ