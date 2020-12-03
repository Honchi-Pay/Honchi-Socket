package com.honchi.socket.domain.chat.repository;

import com.honchi.socket.domain.chat.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, String> {
    Chat findByChatId(String chatId);
    Optional<Chat> findByChatIdAndUserId(String chatId, Integer userId);
    void deleteByChatIdAndUserId(String chatId, Integer userId);
}
