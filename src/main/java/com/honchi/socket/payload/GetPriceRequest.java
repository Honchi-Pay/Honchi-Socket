package com.honchi.socket.payload;

import lombok.Getter;

@Getter
public class GetPriceRequest {

    private String chatId;

    private Integer price;
}
