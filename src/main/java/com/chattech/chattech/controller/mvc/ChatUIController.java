package com.chattech.chattech.controller.mvc;

import com.chattech.chattech.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

// URL: GET http://localhost:8080/admin/chat

@RequestMapping("/ui/chat")
public class ChatUIController {

    private final MessageService messageService;

    // Inyección manual por constructor
    public ChatUIController(MessageService messageService) {
        this.messageService = messageService;

    }
    @GetMapping
    public String showChatRoom(Model model) {
        // Pasamos el historial actual a la vista para que Thymeleaf lo pinte al cargar la página
        model.addAttribute("history", messageService.getHistory());

        return "chat/room";
    }
}