package com.honchi.socket.domain.post;

import com.honchi.socket.domain.post.enums.Category;
import com.honchi.socket.domain.post.enums.Completion;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Double lon;

    @Column(nullable = false)
    private Double lat;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Completion completion;
}
