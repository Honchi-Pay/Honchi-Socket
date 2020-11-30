package com.honchi.socket.domain.chat.repository;

import com.honchi.socket.domain.chat.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, String> {
    Chat findByChatId(String chatId);
    void deleteByChatIdAndUserId(String chatId, Integer userId);
}
