package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    GetUser signUp(@RequestBody SignUp body) {
        User newUser = userRepository.save(new User(body.id(), body.name(), passwordEncoder.encode(body.password())));
        return new GetUser(newUser.getId(), newUser.getName());
    }

    @GetMapping("/users/{userId}/posts")
    List<PostController.GetPost> getPosts(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getPosts()
                .stream()
                .map(post -> new PostController.GetPost(post.getId(), post.getAuthor().getId(), post.getTitle(), post.getContent()))
                .toList();
    }

    public record GetUser(Integer id, String name) {
    }

    public record SignUp(Integer id, String name, String password) {
    }
}
