package com.honchi.socket.domain.post.repository;

import com.honchi.socket.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    void deleteById(Integer postId);
}