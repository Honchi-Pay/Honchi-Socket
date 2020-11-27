package com.honchi.socket.domain.post.repository;

import com.honchi.socket.domain.post.Post;
import com.honchi.socket.domain.post.enums.Completion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByCompletionAndCreatedAtAfter(Completion completion, LocalDateTime localDateTime);

    void deleteById(Integer postId);

    List<Post> findAllByTitleContainsAndCompletionAndCreatedAtAfter(String title, Completion completion, LocalDateTime localDateTime);

    @Query(value = " select * from post" +
            " where id = :postId and (6371 * acos(cos(radians(:userLat)) * cos(radians(:lat)) * cos(radians(:lon) - " +
            "radians(:userLon)) + sin(radians(:userLat)) * sin(radians(:lat)))) <= :dist", nativeQuery = true)
    Optional<Post> findByIdAndLatAndLon(@Param("postId") int postId,
                                        @Param("lat") double lat,
                                        @Param("lon") double lon,
                                        @Param("userLat") double userLat,
                                        @Param("userLon") double userLon,
                                        @Param("dist") int dist);
}