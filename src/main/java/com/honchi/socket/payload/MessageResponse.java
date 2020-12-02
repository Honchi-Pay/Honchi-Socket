package com.honchi.socket.payload;

import com.honchi.socket.domain.message.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {

    private Integer id;

    private Integer userId;

    private String nickName;

    private String message;

    private MessageType messageType;

    private Integer readCount;

    private LocalDateTime time;

    private boolean isDeleted;

    private Integer userId;
}
