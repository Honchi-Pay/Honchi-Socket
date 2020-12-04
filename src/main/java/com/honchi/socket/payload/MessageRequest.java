package com.honchi.socket.payload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequest {

    private String chatId;

    private String message;

    public MessageRequest() {

    }

    public MessageRequest(String chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }
}
