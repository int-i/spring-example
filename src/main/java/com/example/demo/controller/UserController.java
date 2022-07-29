package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{userId}/posts")
    List<Post> getPosts(@PathVariable Integer userId) {
        return userRepository.findById(userId).orElseThrow().getPosts();
    }
}
