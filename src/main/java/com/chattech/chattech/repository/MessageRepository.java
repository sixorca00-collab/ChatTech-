package com.chattech.chattech.repository;


import com.chattech.chattech.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findTop10ByOrderByDayToSendDesc();
}
