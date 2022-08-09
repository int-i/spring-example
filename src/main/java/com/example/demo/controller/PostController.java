package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.entity.PostLike;
import com.example.demo.entity.User;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
public class PostController {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostController(PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @PostMapping("/posts")
    GetPost createPost(@AuthenticationPrincipal User author, @RequestBody CreatePost body) {
        Post newPost = postRepository.save(new Post(author, body.title(), body.content()));
        return new GetPost(newPost.getId(), newPost.getAuthor().getId(), newPost.getTitle(), newPost.getContent());
    }

    @GetMapping("/posts")
    List<GetPost> getPosts() {
        return StreamSupport.stream(postRepository.findAll().spliterator(), false)
                .map(post -> new PostController.GetPost(post.getId(), post.getAuthor().getId(), post.getTitle(), post.getContent()))
                .toList();
    }

    @GetMapping("/posts/{postId}")
    GetPost getPost(@PathVariable Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return new GetPost(post.getId(), post.getAuthor().getId(), post.getTitle(), post.getContent());
    }

    // @GetMapping("/posts/{postId}/comments")
    // List<Comment> getComments(@PathVariable Integer postId) {
    //     return postRepository.findById(postId).orElseThrow().getComments();
    // }

    @PostMapping("/posts/{postId}/likes")
    void likePost(@AuthenticationPrincipal User user, @PathVariable Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        PostLike newLike = new PostLike(post, user);
        if (postLikeRepository.findById(newLike.getId()).isEmpty()) {
            postLikeRepository.save(newLike);
        } else {
            postLikeRepository.deleteById(newLike.getId());
        }
    }

    public record GetPost(Integer id, @JsonProperty("author_id") Integer authorId, String title, String content) {
    }

    public record CreatePost(String title, String content) {
    }
}
