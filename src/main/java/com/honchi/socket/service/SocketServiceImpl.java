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
import com.honchi.socket.payload.ChangeTitleRequest;
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

        checkUser(client, user);

        Authority authority = Authority.MEMBER;
        String title = "default";

        if (!client.getAllRooms().contains(room)) {
            authority = Authority.LEADER;
            title = user.getNickName() + "님의 채팅방";
        }

        chatRepository.save(
                Chat.builder()
                        .userId(user.getId())
                        .roomId(room)
                        .title(title)
                        .authority(authority)
                        .build()
        );

        client.joinRoom(room);
        server.getRoomOperations(room).sendEvent("join", user.getNickName() + "님이 참가하였습니다.");
    }

    @Override
    public void leaveRoom(SocketIOClient client, String room) {
        checkRoom(client, room);

        User user = client.get("user");

        checkUser(client, user);

        client.leaveRoom(room);
        server.getRoomOperations(room).sendEvent("leave", user.getNickName() + "님이 퇴장하였습니다.");
    }

    @Override
    public void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest) {
        checkRoom(client, changeTitleRequest.getRoomId());

        User user = client.get("user");

        checkUser(client, user);

        server.getRoomOperations(changeTitleRequest.getRoomId()).sendEvent("change",
                user.getNickName() + "님이 채팅방 이름을 " +
                        changeTitleRequest.getTitle() + "로 지정하였습니다."
        );
    }

    @Override
    public void send(SocketIOClient client, MessageRequest messageRequest) {
        checkRoom(client, messageRequest.getRoomId());

        User user = client.get("user");

        checkUser(client, user);

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

    private void checkRoom(SocketIOClient client, String roomId) {
        if (!client.getAllRooms().contains(roomId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
        }
    }

    private void checkUser(SocketIOClient client, User user) {
        if (user == null) {
            System.out.println("유저 정보를 찾을 수 없습니다.");
            client.disconnect();
        }
    }
}
