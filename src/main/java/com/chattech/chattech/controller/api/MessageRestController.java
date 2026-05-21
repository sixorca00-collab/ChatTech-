package com.chattech.chattech.controller.api;

import com.chattech.chattech.model.Message;
import com.chattech.chattech.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> history = messageService.getHistory();
        return ResponseEntity.ok(history);
    }
}
