package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductPage;
import com.example.demo.entity.User;
import com.example.demo.entity.UserPage;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;
//    @Value("${hostname}")
//    private String hostname;
    private void encoderPassword(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    public User SaveUser(User user){
        encoderPassword(user);
       // user.setCreatedTime(new Date());
        return repository.save(user);
    }
    public User getuserById(int id){
        return repository.findById(id).get();
    }
    public User findByPhone(String phone) throws Exception {
        return repository.findByPhone(phone);
    }
    public Page<User> getUsers(UserPage userPage){
        Sort sort = Sort.by(userPage.getSortDirection() , userPage.getSortBy());
        Pageable pageable = PageRequest.of(userPage.getPageNumber() ,
                userPage.getPageSize() , sort );
        return  repository.findAll(pageable);
    }
}
