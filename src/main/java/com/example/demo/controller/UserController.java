package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductPage;
import com.example.demo.entity.User;
import com.example.demo.entity.UserPage;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    JavaMailSender javaMailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService service;
    @GetMapping("/allUsers")
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(UserPage userPage) {
        return new ResponseEntity<>(service.getUsers(userPage) , HttpStatus.OK);
    }
    //create new employee
    @PostMapping("/users")
    public ResponseEntity<User> addEmployee(@RequestBody User user) {
        User newUser = service.SaveUser(user);
        LOGGER.info("Created " + newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    // get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User could not find : " + id));
        return ResponseEntity.ok(user);
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id ,
                                           @RequestBody User userDetails){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user by id : " + id));
        user.setUserName(userDetails.getUserName());
        user.setPassword(userDetails.getPassword());
        user.setPhone(userDetails.getPhone());
        user.setEmail(userDetails.getEmail());
        user.setAddress(userDetails.getAddress());
        user.setUpdatedTime(new Date());
        User updatedUser = userRepository.save(user);
        LOGGER.info(updatedUser + "updated");
        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User is not exist with id : " + id));
        userRepository.deleteById(id);
        Map<String , Boolean> response = new HashMap<>();
        response.put("delete" , Boolean.TRUE);
        LOGGER.info(user + " deleted");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/send")
    public ResponseEntity<?> submitContactForm(@RequestBody HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String mailSubject = fullName + " has sent a message";
        String mailContent = "<p><b>Send name</b> : " + fullName + "</p>";
        mailContent += "<p><b>Sender E-Mail </b>: " + email + "</p>";
        mailContent += "<p><b>Subject </b>: " + subject + "</p>";
        mailContent += "<p><b>Content </b>: " + content + "</p>";
        helper.setFrom("dongnguyen@gmail.com", "Mobile Shop");
        helper.setTo("dong190699@gmail.com");
        helper.setSubject(mailSubject);
        helper.setText(mailContent, true);
//        if (!multipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            InputStreamSource source = new InputStreamSource() {
//                @Override
//                public InputStream getInputStream() throws IOException {
//                    return multipartFile.getInputStream();
//                }
//            };
//            helper.addAttachment(fileName, source);
//        }
        javaMailSender.send(message);
        return ResponseEntity.ok(message);
    }
}
