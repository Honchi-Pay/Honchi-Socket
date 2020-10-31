package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class MessageRequest {
    
    private String room;
    private String message;
}
