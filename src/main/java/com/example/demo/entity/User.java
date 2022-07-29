package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "users")
public class User {
    @Id
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    public Integer getId() {
        return id;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
