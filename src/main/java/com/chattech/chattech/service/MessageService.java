package com.chattech.chattech.service;


import com.chattech.chattech.model.Message;
import com.chattech.chattech.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    private  MessageRepository repository;

    MessageService(MessageRepository repository){
        this.repository = repository;
    }

    public Message sendMessage(Message message){
        return repository.save(message);
    }

    public List<Message> getHistory(){

        List<Message> top = repository.findTop10ByOrderByDayToSendDesc();

        Collections.reverse(top);
        return top;
    }



}
