package com.kim.SpringStudy.service;


import com.kim.SpringStudy.domain.User;
import com.kim.SpringStudy.repository.UserRepository;
import com.kim.SpringStudy.domain.Sales;
import com.kim.SpringStudy.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor


public class SalesService {

    private final UserService userService;
    private final SalesRepository salesRepository;
    private final UserRepository userRepository;
    public void orderItem(String title, Integer price, Integer count){

        //로그인한 사람 유저정보 가져오기
        User user = userService.getCurrentLoggedInUser();
        //sales 서비스 작성
        Sales sale = new Sales();
        sale.setItemName(title);
        sale.setPrice(price);
        sale.setCount(count);
        sale.setUser(user);
        salesRepository.save(sale);
        System.out.println("저장완료" + salesRepository.save(sale));
    }

    //사용자 정보 확인 후 주문 목록 리턴
    public List<Sales> getMyOrders(String username){
        return salesRepository.findByUsername(username);
    }



}
