package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order , Integer> {
    @Query("Select o  FROM Order o WHERE o.user_id=:user_id")
    List<Order> getByuserId(@Param("user_id") Integer user_id);
    @Query("select distinct user_id , address , orderDate from Order ")
    List<?> getDistinctByUser_id();

    @Modifying
    @Transactional
    @Query("DELETE  FROM Order o WHERE o.user_id =:user_id")
    void deleteOrderByUser_id(@Param("user_id")Integer user_id);
}
