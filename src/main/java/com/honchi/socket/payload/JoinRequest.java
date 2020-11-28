package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class JoinRequest {

    private String chatId;

    private Integer userId;

    private Integer postId;
}
