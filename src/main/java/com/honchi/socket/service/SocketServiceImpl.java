package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.honchi.socket.domain.user.User;
import com.honchi.socket.domain.user.repository.UserRepository;
import com.honchi.socket.payload.MessageRequest;
import com.honchi.socket.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final SocketIOServer server;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(!jwtTokenProvider.validationToken(token)) {
            System.out.println("토큰이 유효하지 않습니다.");
            client.disconnect();
        }

        User user;
        try {
            user = userRepository.findByEmail(jwtTokenProvider.getEmail(token)).get();
            client.set("user", user);
            System.out.println("ConnectedId : " + client.getSessionId() + " UserNickName : " + user.getNickName());
        } catch (Exception e) {
            System.out.println("유저를 찾을 수 없습니다.");
            client.disconnect();
        }
    }

    @Override
    public void disConnect(SocketIOClient client) {
        System.out.println("DisconnectedId : " + client.getSessionId());
    }

    @Override
    public void send(SocketIOClient client, MessageRequest messageRequest) {
        User user = client.get("user");
    }
}
