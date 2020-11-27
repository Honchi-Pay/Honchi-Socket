package com.honchi.socket.domain.user;

import com.honchi.socket.domain.chat.Chat;
import com.honchi.socket.domain.post.Post;
import com.honchi.socket.domain.post.PostAttend;
import com.honchi.socket.domain.user.enums.Sex;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 8)
    private String nickName;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Double lat;

    private Double lon;
}
