package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.honchi.socket.payload.MessageRequest;

public interface SocketService {

    void connect(SocketIOClient client);
    void disConnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, String room);
    void send(SocketIOClient client, MessageRequest messageRequest);
}
