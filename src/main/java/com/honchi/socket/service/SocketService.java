package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.honchi.socket.payload.*;

public interface SocketService {

    void connect(SocketIOClient client);
    void disConnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, JoinRequest joinRequest);
    void leaveRoom(SocketIOClient client, String chatId);
    void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest);
    void sendMessage(SocketIOClient client, MessageRequest messageRequest);
    void sendImage(SocketIOClient client, ImageRequest imageRequest);
    void getPrice(SocketIOClient client, GetPriceRequest getPriceRequest);
}
