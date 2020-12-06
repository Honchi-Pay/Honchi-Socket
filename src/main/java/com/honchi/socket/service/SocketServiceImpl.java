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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
            user = userRepository.findByEmail(jwtTokenProvider.getEmail(token))
                    .orElseThrow(RuntimeException::new);
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
    public void joinRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        Optional<Chat> oChat = chatRepository.findByChatIdAndUserId(chatId, user.getId());
        if(oChat.isPresent()) {
            printLog(chatId, user, "already joined user : ");
            client.disconnect();
            return;
        }

        Authority authority = Authority.MEMBER;
        String title = "";

        if (oChat.isPresent()) {
            Chat chat = chatRepository.findByChatId(chatId);
            title = chat.getTitle();
        } else {
            authority = Authority.LEADER;
            title = user.getNickName() + "님의 채팅방";
        }

        client.joinRoom(chatId);

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(chatId)
                        .message("님이 참가하였습니다.")
                        .messageType(MessageType.INFO)
                        .readCount(0)
                        .isDelete(false)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .build()
        );

        chatRepository.save(
                Chat.builder()
                        .userId(user.getId())
                        .chatId(chatId)
                        .title(title)
                        .readPoint(message.getId())
                        .authority(authority)
                        .build()
        );
        printLog(chatId, user, " join User : ");

        sendInfo(user, message);
    }

    @Override
    public void leaveRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        if(client.getAllRooms().contains("" + chatId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        client.leaveRoom(chatId);
        chatRepository.deleteByChatIdAndUserId(chatId, user.getId());

        printLog(chatId, user, " leave User : ");
        Message message = messageRepository.save(
                Message.builder()
                        .chatId(chatId)
                        .message("님이 퇴장하였습니다.")
                        .messageType(MessageType.INFO)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .isDelete(false)
                        .readCount(0)
                        .build()
        );

        sendInfo(user, message);
    }

    @Override
    public void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest) {
        String chatId = changeTitleRequest.getChatId();
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        printLog(chatId, user, " updateTitle User : ");
        Message message = messageRepository.save(
                Message.builder()
                        .chatId(changeTitleRequest.getChatId())
                        .message(user.getNickName() + "님이 채팅방 이름을 " + changeTitleRequest.getTitle() + "로 지정하였습니다.")
                        .messageType(MessageType.INFO)
                        .userId(user.getId())
                        .time(LocalDateTime.now())
                        .isDelete(false)
                        .readCount(0)
                        .build()
        );

        sendInfo(user, message);
    }

    @Override
    public void sendMessage(SocketIOClient client, MessageRequest messageRequest) {
        String chatId = messageRequest.getChatId();
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        printLog(chatId, user, " send User : ");

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(messageRequest.getChatId())
                        .message(messageRequest.getMessage())
                        .messageType(MessageType.MESSAGE)
                        .time(LocalDateTime.now())
                        .userId(user.getId())
                        .readCount(chatRepository.countByChatId(chatId))
                        .isDelete(false)
                        .build()
        );

        send(user, message);
    }

    @Override
    public void sendImage(SocketIOClient client, ImageRequest imageRequest) {
        String chatId = imageRequest.getChatId();
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        printLog(chatId, user, " sendImage User : ");

        messageRepository.findById(imageRequest.getMessageId()).ifPresent(message -> {
            send(user, message);
        });
    }

    @Async
    @Override
    public void getPrice(SocketIOClient client, GetPriceRequest getPriceRequest) {
        String chatId = getPriceRequest.getChatId();
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저 정보가 없습니다.");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 존재하지 않습니다.");
            client.disconnect();
            return;
        }

        Message message = messageRepository.save(
                Message.builder()
                        .chatId(getPriceRequest.getChatId())
                        .message("님이 " + getPriceRequest.getPrice() + "원을 입력하였습니다.")
                        .messageType(MessageType.INFO)
                        .readCount(0)
                        .userId(user.getId())
                        .isDelete(false)
                        .time(LocalDateTime.now())
                        .build()
        );

        sendInfo(user, message);
    }

    private void sendInfo(User user, Message message) {
        server.getRoomOperations(message.getChatId()).sendEvent("info",
                MessageResponse.builder()
                        .id(message.getId())
                        .message(user.getNickName() + message.getMessage())
                        .messageType(message.getMessageType())
                        .build()
        );
    }

    private void send(User user, Message message) {
        server.getRoomOperations(message.getChatId()).sendEvent("receive",
                MessageResponse.builder()
                        .id(message.getId())
                        .userId(message.getUserId())
                        .nickName(user.getNickName())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .readCount(message.getReadCount())
                        .time(message.getTime())
                        .isDelete(message.isDelete())
                        .build()
        );
    }

    private void printLog(String chatId, User user, String message) {
        String stringDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println("[" + stringDate + "] " + "chatId:" + chatId + message + user.getNickName());
    }
}
