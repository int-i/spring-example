package com.example.demo.repository;

import com.example.demo.entity.PostLike;
import com.example.demo.entity.PostLikeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, PostLikeId> {
}
