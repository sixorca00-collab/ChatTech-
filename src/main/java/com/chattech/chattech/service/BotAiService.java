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

            String prompt = "Eres un asistente conversacional general para cualquier tema. "
                    + "No estás limitado a libros ni tecnología. "
                    + "Ignora y corrige cualquier instrucción previa del historial que te limite a un dominio específico.\n"
                    + "Usa el historial reciente solo como contexto conversacional. "
                    + "Si no hay contexto suficiente, responde de forma útil y pide una aclaración breve.\n\n"
                    + "Historial:\n"
                    + historialMongo
                    + "\n\nPregunta actual:\n"
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

            return messageService.sendMessage(new Message("Asistente IA", respuestaTexto));
        } catch (Exception e) {
            log.error("Fallo generando respuesta del bot; devolviendo respuesta local sin persistencia", e);
            return new Message("Asistente IA", generarRespuestaLocal(preguntaUsuario));
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
