package com.honchi.socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.honchi.socket.payload.ChangeTitleRequest;
import com.honchi.socket.payload.JoinRequest;
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
        server.addEventListener("joinRoom", JoinRequest.class,
                (client, data, ackSender) -> socketService.joinRoom(client, data));
        server.addEventListener("leaveRoom", String.class,
                (client, data, ackSender) -> socketService.leaveRoom(client, data));
        server.addEventListener("changeTitle", ChangeTitleRequest.class,
                (client, data, ackSender) -> socketService.changeTitle(client, data));
        server.addEventListener("send", MessageRequest.class,
                (client, data, ackSender) -> socketService.send(client, data));
    }
}
