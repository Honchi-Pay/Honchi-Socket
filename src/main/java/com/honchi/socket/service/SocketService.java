package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.honchi.socket.payload.ChangeTitleRequest;
import com.honchi.socket.payload.ImageRequest;
import com.honchi.socket.payload.JoinRequest;
import com.honchi.socket.payload.MessageRequest;

public interface SocketService {

    void connect(SocketIOClient client);
    void disConnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, JoinRequest joinRequest);
    void leaveRoom(SocketIOClient client, String chatId);
    void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest);
    void sendMessage(SocketIOClient client, MessageRequest messageRequest);
    void sendImage(SocketIOClient client, ImageRequest imageRequest);
}
