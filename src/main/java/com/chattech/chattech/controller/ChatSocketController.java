package com.chattech.chattech.controller;

import com.chattech.chattech.model.Message;
import com.chattech.chattech.service.MessageService;
import com.chattech.chattech.service.BotAiService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {
    private static final Logger log = LoggerFactory.getLogger(ChatSocketController.class);

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

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message processUserMessage(Message receivedMessage) {

        Message savedMessage = messageService.sendMessage(receivedMessage);

        Thread.startVirtualThread(() -> {
            try {
                Message aiResponse = botAiService.generarRespuestaIA(savedMessage.getContent());
                messagingTemplate.convertAndSend("/topic/messages", aiResponse);
            } catch (Exception e) {
                log.error("Error processing AI response asynchronously", e);
                Message errorMessage = new Message();
                errorMessage.setReceptor("LibroBot IA");
                errorMessage.setContent("No pude generar respuesta en este momento. Revisa logs del servidor para el detalle.");
                messagingTemplate.convertAndSend("/topic/messages", errorMessage);
            }
        });

        return savedMessage;
    }
}
