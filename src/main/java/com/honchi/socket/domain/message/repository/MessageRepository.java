package com.honchi.socket.domain.message.repository;

import com.honchi.socket.domain.message.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    Message findTop1ByChatIdOrderByTimeDesc(String chatId);
}
