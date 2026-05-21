package com.chattech.chattech.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String receptor;
    private String content;
    private LocalDateTime dayToSend;

    public Message(){}

    public Message(String receptor, String content){
        this.receptor = receptor;
        this.content = content;
        this.dayToSend = LocalDateTime.now();
    }
    public String getId() {
        return id;
    }

    public String getReceptor() {
        return receptor;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDayToSend() {
        return dayToSend;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDayToSend(LocalDateTime dayToSend) {
        this.dayToSend = dayToSend;
    }
}
