package com.honchi.socket.domain.chat;

import com.honchi.socket.domain.chat.enums.Authority;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String chatId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private Integer readPoint;
}
