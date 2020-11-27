package com.honchi.socket.domain.message;

import com.honchi.socket.domain.message.enums.MessageType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private Integer userId;

    private String roomId;

    private String message;

    private MessageType messageType;

    private Integer readCount;

    private LocalDateTime time;

    private boolean isDelete;
}
