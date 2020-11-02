package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.honchi.socket.domain.chat.Chat;
import com.honchi.socket.domain.chat.enums.Authority;
import com.honchi.socket.domain.chat.repository.ChatRepository;
import com.honchi.socket.domain.message.Message;
import com.honchi.socket.domain.message.repository.MessageRepository;
import com.honchi.socket.domain.user.User;
import com.honchi.socket.domain.user.repository.UserRepository;
import com.honchi.socket.payload.MessageRequest;
import com.honchi.socket.payload.MessageResponse;
import com.honchi.socket.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final SocketIOServer server;

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if (!jwtTokenProvider.validationToken(token)) {
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
    public void joinRoom(SocketIOClient client, String room) {
        User user = client.get("user");

        if (user == null) {
            System.out.println("유저 정보가 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        Authority authority = Authority.MEMBER;

        if (!client.getAllRooms().contains(room)) {
            authority = Authority.LEADER;
        }

        chatRepository.save(
                Chat.builder()
                        .userId(user.getId())
                        .roomId(room)
                        .title("default")
                        .authority(authority)
                        .build()
        );

        client.joinRoom(room);
        server.getRoomOperations(room).sendEvent("join room", user.getNickName() + "님이 참가하였습니다.");
    }

    @Override
    public void send(SocketIOClient client, MessageRequest messageRequest) {
        if (!client.getAllRooms().contains(messageRequest.getRoomId())) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
        }

        User user = client.get("user");
        if (user == null) {
            System.out.println("유저 정보가 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        Message message = messageRepository.save(
                Message.builder()
                        .roomId(messageRequest.getRoomId())
                        .message(messageRequest.getMessage())
                        .time(LocalDateTime.now())
                        .userId(user.getId())
                        .isDelete(false)
                        .isShow(false)
                        .build()
        );

        server.getRoomOperations(message.getRoomId()).sendEvent("receive",
                MessageResponse.builder()
                        .id(message.getId())
                        .name(user.getNickName())
                        .message(message.getMessage())
                        .time(message.getTime())
                        .isDeleted(message.isDelete())
                        .userId(user.getId())
                        .build()
        );
    }
}
