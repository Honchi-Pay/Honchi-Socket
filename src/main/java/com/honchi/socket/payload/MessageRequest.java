package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class MessageRequest {

    private String chatId;

    private String message;

    public MessageRequest(String chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }
}
