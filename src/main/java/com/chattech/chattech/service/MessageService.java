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

    //Guardar mensajes en la db
    public Message sendMessage(Message message){
        return repository.save(message);
    }

    //Obtener contexto reciente para la IA
    public List<Message> getHistory(){

        //Los ultimos 10 con el metodo en la interfaz
        List<Message> top = repository.findTop10OrderDaySendDesc();

        //Invertimos
        Collections.reverse(top);
        return top;
    }



}

