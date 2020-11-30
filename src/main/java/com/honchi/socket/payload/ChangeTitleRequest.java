package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class ChangeTitleRequest {

    private String chatId;

    private String title;
}
