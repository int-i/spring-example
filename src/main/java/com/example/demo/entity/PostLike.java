package com.example.demo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "post_likes")
public class PostLike {
    @EmbeddedId
    private PostLikeId id;

    public PostLike(PostLikeId id) {
        this.id = id;
    }

    public PostLike(Post post, User user) {
        id.setPost(post);
        id.setUser(user);
    }

    public PostLike() {
    }

    public PostLikeId getId() {
        return id;
    }

    public void setId(PostLikeId id) {
        this.id = id;
    }

    public Post getPost() {
        return id.getPost();
    }

    public void setPost(Post post) {
        id.setPost(post);
    }

    public User getUser() {
        return id.getUser();
    }

    public void setUser(User user) {
        id.setUser(user);
    }
}
