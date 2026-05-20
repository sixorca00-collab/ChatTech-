package com.chattech.chattech.service;

import com.chattech.chattech.model.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class BotAiService {

    // Definimos los atributos como 'final' para garantizar la inmutabilidad
    private final ChatClient chatClient;
    private final MessageService messageService;

    public BotAiService(ChatClient.Builder chatClientBuilder, MessageService messageService) {
        this.chatClient = chatClientBuilder.build();
        this.messageService = messageService;
    }

    public Message generarRespuestaIA(String preguntaUsuario) {
        //  Extraer el contexto de la base de datos (Últimos 10 mensajes)
        String historialMongo = messageService.getHistory()
                .stream()
                .map(m -> m.getReceptor() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));

        // Construir el Prompt del sistema combinando el historial y la nueva pregunta
        String prompt = "Eres el asistente de LibroTech llamado LibroBot IA. Usa este historial de chat "
                + "reciente como tu contexto y memoria para entender la conversación:\n"
                + historialMongo + "\n\nResponde de manera concisa y profesional a la siguiente duda: "
                + preguntaUsuario;

        // Llamar a Spring AI mediante la API fluida de ChatClient
        String respuestaTexto = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // Crear, persistir en MongoDB y retornar el mensaje generado por el Bot
        Message mensajeBot = new Message();
        mensajeBot.setReceptor("LibroBot IA");
        mensajeBot.setContent(respuestaTexto);

        return messageService.sendMessage(mensajeBot);
    }
}