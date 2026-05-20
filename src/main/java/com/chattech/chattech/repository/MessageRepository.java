package com.chattech.chattech.repository;


import com.chattech.chattech.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    //Custom query para los ultimos 10 mensajes ordenador por fecha.
    List<Message> findTop10OrderDaySendDesc();
}
