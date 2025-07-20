package com.kim.SpringStudy.service;


import com.kim.SpringStudy.domain.User;
import com.kim.SpringStudy.repository.ItemRepository;
import com.kim.SpringStudy.repository.UserRepository;
import com.kim.SpringStudy.domain.Sales;
import com.kim.SpringStudy.repository.SalesRepository;
import jakarta.transaction.Transactional;
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
    private final ItemRepository itemRepository;

    //상품 주문 기능
    @Transactional // 데이터 조작시 예외 발생시 알아서 롤백해줌
    public void orderItem(String title, Integer price, Integer count, long id) {

        // 1. 상품 조회 및 재고 차감 (트랜젝션 1)
        var result = itemRepository.findById(id);
        if (result.isPresent()) {
            var item = result.get();

            if (item.getCount() <= 0) {
                throw new IllegalStateException("상품 재고 부족");
            }
            item.setCount(item.getCount() - count); // 구매 수량만큼 차감
            itemRepository.save(item);
//            if (true) {
//                throw new RuntimeException("트랜잭션 테스트중임");
//            }
            // 2. 주문 저장 (트렌젝션 2)
            User user = userService.getCurrentLoggedInUser();
            Sales sale = new Sales();
            sale.setItemName(title); // 아이템 명은 무엇인지
            sale.setPrice(price); // 얼마인지
            sale.setCount(count); // 몇개 샀는지
            sale.setUser(user); //누가 샀는지
            salesRepository.save(sale);

            System.out.println("주문 및 재고 차감 완료");
        } else {
            System.out.println("존재하지 않은 아이템임");
        }


    }

    //사용자 정보 확인 후 주문 목록 리턴
    public List<Sales> getMyOrders(String username) {
        return salesRepository.findByUsername(username);
    }


}
