package com.chattech.chattech.controller.mvc;

import com.chattech.chattech.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/chat")
public class ChatUIController {

    private final MessageService messageService;

    public ChatUIController(MessageService messageService) {
        this.messageService = messageService;

    }
    @GetMapping
    public String showChatRoom(Model model) {
        model.addAttribute("history", messageService.getHistory());

        return "chat/room";
    }
}
