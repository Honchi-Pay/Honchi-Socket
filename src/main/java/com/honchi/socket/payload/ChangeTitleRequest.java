package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class ChangeTitleRequest {

    private String roomId;

    private String title;
}
