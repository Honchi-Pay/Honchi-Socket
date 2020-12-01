package com.honchi.socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.honchi.socket.domain.chat.Chat;
import com.honchi.socket.domain.chat.enums.Authority;
import com.honchi.socket.domain.chat.repository.ChatRepository;
import com.honchi.socket.domain.message.Message;
import com.honchi.socket.domain.message.enums.MessageType;
import com.honchi.socket.domain.message.repository.MessageRepository;
import com.honchi.socket.domain.user.User;
import com.honchi.socket.domain.user.repository.UserRepository;
import com.honchi.socket.payload.*;
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
    public void joinRoom(SocketIOClient client, JoinRequest joinRequest) {
        User user = client.get("user");
        String room = joinRequest.getChatId();

        checkUser(client, user);

        Authority authority = Authority.MEMBER;
        String title = "";

        if (!client.getAllRooms().contains(room)) {
            authority = Authority.LEADER;
            title = user.getNickName() + "님의 채팅방";
        } else {
            Chat chat = chatRepository.findByChatId(joinRequest.getChatId());
            title = chat.getTitle();
        }

        chatRepository.save(
                Chat.builder()
                        .userId(user.getId())
                        .chatId(room)
                        .postId(joinRequest.getPostId())
                        .title(title)
                        .authority(authority)
                        .build()
        );

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(joinRequest.getChatId())
                        .message(user.getNickName() + "님이 참가하였습니다.")
                        .messageType(MessageType.INFO)
                        .readCount(0)
                        .isDelete(false)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .build()
        );

        client.joinRoom(room);
        server.getRoomOperations(room).sendEvent("join",
                MessageResponse.builder()
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .build()
        );
    }

    @Override
    public void leaveRoom(SocketIOClient client, String chatId) {
        checkRoom(client, chatId);

        User user = client.get("user");

        checkUser(client, user);

        chatRepository.deleteByChatIdAndUserId(chatId, user.getId());

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(chatId)
                        .message(user.getNickName() + "님이 퇴장하였습니다.")
                        .messageType(MessageType.INFO)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .isDelete(false)
                        .readCount(0)
                        .build()
        );

        client.leaveRoom(chatId);
        server.getRoomOperations(chatId).sendEvent("leave",
                MessageResponse.builder()
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .build()
        );
    }

    @Override
    public void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest) {
        checkRoom(client, changeTitleRequest.getChatId());

        User user = client.get("user");

        checkUser(client, user);

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(changeTitleRequest.getChatId())
                        .message(user.getNickName() + "님이 채팅방 이름을 " +
                                changeTitleRequest.getTitle() + "로 지정하였습니다.")
                        .messageType(MessageType.INFO)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .isDelete(false)
                        .readCount(0)
                        .build()
        );

        server.getRoomOperations(changeTitleRequest.getChatId()).sendEvent("change",
                MessageResponse.builder()
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .build()
        );
    }

    @Override
    public void sendMessage(SocketIOClient client, MessageRequest messageRequest) {
        checkRoom(client, messageRequest.getChatId());

        User user = client.get("user");

        checkUser(client, user);

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(messageRequest.getChatId())
                        .message(messageRequest.getMessage())
                        .messageType(MessageType.MESSAGE)
                        .time(LocalDateTime.now())
                        .userId(user.getId())
                        .isDelete(false)
                        .build()
        );

        send(user, message);
    }

    @Override
    public void sendImage(SocketIOClient client, ImageRequest imageRequest) {
        checkRoom(client, imageRequest.getChatId());

        User user = client.get("user");

        checkUser(client, user);

        messageRepository.findById(imageRequest.getMessageId()).ifPresent(message -> {
            send(user, message);
        });
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

    private void send(User user, Message message) {
        server.getRoomOperations(message.getChatId()).sendEvent("receive",
                MessageResponse.builder()
                        .id(message.getId())
                        .name(user.getNickName())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .time(message.getTime())
                        .isDeleted(message.isDelete())
                        .userId(message.getUserId())
                        .build()
        );
    }
}
