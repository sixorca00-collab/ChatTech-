package com.chattech.chattech.controller;

import com.chattech.chattech.model.Message;
import com.chattech.chattech.service.MessageService;
import com.chattech.chattech.service.BotAiService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    private final MessageService messageService;
    private final BotAiService botAiService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(MessageService messageService,
                                BotAiService botAiService,
                                SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.botAiService = botAiService;
        this.messagingTemplate = messagingTemplate;
    }

    // CORREGIDO: De /enviar a /send, y de /tema/mensajes a /topic/messages
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message processUserMessage(Message receivedMessage) {

        Message savedMessage = messageService.sendMessage(receivedMessage);

        Thread.startVirtualThread(() -> {
            try {
                Message aiResponse = botAiService.generarRespuestaIA(savedMessage.getContent());

                // CORREGIDO: Envío asíncrono al broker en inglés
                messagingTemplate.convertAndSend("/topic/messages", aiResponse);
            } catch (Exception e) {
                System.err.println("Error processing AI response asynchronously: " + e.getMessage());
            }
        });

        return savedMessage;
    }
}