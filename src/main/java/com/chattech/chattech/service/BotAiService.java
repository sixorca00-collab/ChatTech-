package com.chattech.chattech.service;

import com.chattech.chattech.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class BotAiService {
    private static final Logger log = LoggerFactory.getLogger(BotAiService.class);

    private final ChatClient chatClient;
    private final MessageService messageService;

    public BotAiService(ChatClient.Builder chatClientBuilder, MessageService messageService) {
        this.chatClient = chatClientBuilder.build();
        this.messageService = messageService;
    }

    public Message generarRespuestaIA(String preguntaUsuario) {
        try {
            String historialMongo = messageService.getHistory()
                    .stream()
                    .map(m -> m.getReceptor() + ": " + m.getContent())
                    .collect(Collectors.joining("\n"));

            String prompt = "Eres el asistente de LibroTech llamado LibroBot IA. Usa este historial de chat "
                    + "reciente como tu contexto y memoria para entender la conversación:\n"
                    + historialMongo + "\n\nResponde de manera concisa y profesional a la siguiente duda: "
                    + preguntaUsuario;

            String respuestaTexto;
            try {
                respuestaTexto = chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();
            } catch (Exception e) {
                log.warn("Fallo proveedor IA externo, usando respuesta local de contingencia: {}", e.getMessage());
                respuestaTexto = generarRespuestaLocal(preguntaUsuario);
            }

            return messageService.sendMessage(new Message("LibroBot IA", respuestaTexto));
        } catch (Exception e) {
            log.error("Fallo generando respuesta del bot; devolviendo respuesta local sin persistencia", e);
            return new Message("LibroBot IA", generarRespuestaLocal(preguntaUsuario));
        }
    }

    private String generarRespuestaLocal(String preguntaUsuario) {
        String pregunta = preguntaUsuario == null ? "" : preguntaUsuario.trim();
        if (pregunta.isEmpty()) {
            return "Estoy en modo local de contingencia. Escribe una pregunta y te ayudo.";
        }
        return "Modo contingencia activo por límite de cuota del proveedor IA. Resumen: "
                + pregunta
                + ". Recomendación: valida requerimientos, divide en pasos y ejecuta primero el de mayor impacto.";
    }
}
