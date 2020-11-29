package com.honchi.socket.domain.message;

import com.honchi.socket.domain.message.enums.MessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String chatId;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private Integer readCount;

    private LocalDateTime time;

    private boolean isDelete;
}
