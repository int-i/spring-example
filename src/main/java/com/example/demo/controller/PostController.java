package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private static class CreatePost {
        private Integer author; // user id

        private String title;

        private String content;

        public Integer getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/posts")
    Post createPost(@RequestBody CreatePost body) {
        User author = userRepository.findById(body.getAuthor()).orElseThrow();
        Post newPost = new Post(author, body.getTitle(), body.getContent());
        return postRepository.save(newPost);
    }

    @GetMapping("/posts/{postId}")
    Post getPost(@PathVariable Integer postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    @GetMapping("/posts/{postId}/comments")
    List<Comment> getComments(@PathVariable Integer postId) {
        return postRepository.findById(postId).orElseThrow().getComments();
    }
}
