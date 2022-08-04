package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostLike;
import com.example.demo.entity.User;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostController(UserRepository userRepository, PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @PostMapping("/posts")
    String createPost(@RequestBody CreatePost body) {
        System.out.println("Create post");
        User author = userRepository.findById(body.getAuthor()).orElseThrow();
        Post newPost = new Post(author, body.getTitle(), body.getContent());
        postRepository.save(newPost);
        return "OK";
    }

    @GetMapping("/posts/{postId}")
    Post getPost(@PathVariable Integer postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    @GetMapping("/posts/{postId}/comments")
    List<Comment> getComments(@PathVariable Integer postId) {
        return postRepository.findById(postId).orElseThrow().getComments();
    }

    @PostMapping("/posts/{postId}/like/{userId}")
    void likePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        PostLike newLike = new PostLike(post, user);
        if (postLikeRepository.findById(newLike.getId()).isEmpty()) {
            postLikeRepository.save(newLike);
        } else {
            // delete
        }
    }

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
}
