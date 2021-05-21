package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("http://localhost:4200")
public interface UserRepository extends PagingAndSortingRepository<User , Integer> {
    @Query("select u from User u where u.email = ?1")
    User getUserByEmail(String email);

    User findByPhone(String phone);

    Boolean existsByEmail(String email);
    @Query("select u from User  u where u.userName = ?1")
    User getUserByUserName(String username);
    Boolean existsByUserName(String username);
}
