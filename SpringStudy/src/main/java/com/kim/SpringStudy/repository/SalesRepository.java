package com.kim.SpringStudy.repository;
import java.util.List;

import com.kim.SpringStudy.domain.User;
import com.kim.SpringStudy.domain.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByUser(User user);

    //상품 다 가져오는 쿼리문(JPQL사용 - join fetch)
    //원래 같으면 모두 select문을 여러개 날리지만 join fetch를 통해 하나로 날림
    @Query("SELECT s FROM Sales s JOIN FETCH s.user WHERE s.user.username = :username")
    List<Sales> findByUsername(@Param("username") String username);

}
