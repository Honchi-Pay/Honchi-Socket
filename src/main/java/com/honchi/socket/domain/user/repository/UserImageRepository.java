package com.honchi.socket.domain.user.repository;

import com.honchi.socket.domain.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    UserImage findByUserId(Integer userId);
}
