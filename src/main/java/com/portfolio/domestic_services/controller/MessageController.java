package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;
import com.portfolio.domestic_services.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(MessageService messageService,
                             SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/my-chats/{userId}")
    public List<ChatListDTO> getMyChats(@PathVariable Long userId) {
        return messageService.getChatsByUser(userId);
    }

    @Operation(summary = "Obtener los mensajes de una visita")
    @ApiResponse(responseCode = "200", description = "Mensajes obtenidos")
    @GetMapping("/call/{callId}")
    public List<Message> getMessages(@PathVariable Long callId) {
        return messageService.getMessagesByCall(callId);
    }

    @PostMapping("/seen/{callId}/{userId}")
    public ResponseEntity<?> markAsSeen(@PathVariable Long callId, @PathVariable Long userId) {
        messageService.markMessagesAsSeen(callId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Enviar un mensaje y notificar por WebSocket")
    @ApiResponse(responseCode = "200", description = "Mensaje guardado y enviado en tiempo real")
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO dto) {

        // 1️⃣ Guardar en Base de Datos (Esto ya lo tenías)
        Message saved = messageService.send(dto);

        // 2️⃣ Notificar a la SALA DE CHAT (Para quien tiene el chat abierto)
        messagingTemplate.convertAndSend(
                "/topic/chat/" + dto.getCallId(),
                saved
        );

        // 3️⃣ NUEVO: Notificar al USUARIO RECEPTOR (Para que le aparezca el globito en "Mis Chats")

        // Obtenemos la visita (Call) asociada al mensaje
        Call call = saved.getCall();

        // Calculamos quién es el destinatario
        // Si yo soy el autor, el destinatario es el 'otro'
        Long receiverId;
        if (saved.getAuthorId().equals(call.getClient().getId())) {
            receiverId = call.getProvider().getId();
        } else {
            receiverId = call.getClient().getId();
        }

        // Enviamos el mensaje al canal personal de notificaciones del usuario
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiverId,
                saved
        );

        return ResponseEntity.ok(saved);
    }

}