package com.kim.SpringStudy.sales;
import java.util.List;

import com.kim.SpringStudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales , Long> {
    List<Sales> findByUser(User user);

}
