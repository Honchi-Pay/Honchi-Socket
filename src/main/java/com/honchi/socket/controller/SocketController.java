package com.honchi.socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.honchi.socket.payload.MessageRequest;
import com.honchi.socket.service.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer server;

    private final SocketService socketService;

    @PostConstruct
    public void socket() {
        server.addConnectListener(socketService::connect);
        server.addDisconnectListener(socketService::disConnect);
        server.addEventListener("send", MessageRequest.class,
                (client, data, ackSender) -> socketService.send(client, data));
    }
}
