package com.honchi.socket.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    private String chatId;

    private String message;
}
