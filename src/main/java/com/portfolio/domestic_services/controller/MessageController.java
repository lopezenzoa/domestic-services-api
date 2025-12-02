package com.portfolio.domestic_services.controller;

import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;
import com.portfolio.domestic_services.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService service;
    @GetMapping("/my-chats/{userId}")
    public List<Call> getMyChats(@PathVariable Long userId) {
        return service.getChatsByUser(userId);
    }

    @Operation(summary = "Obtener los mensajes de una visita")
    @ApiResponse(responseCode = "200", description = "Mensajes obtenidos")
    @GetMapping("/call/{callId}")
    public List<Message> getMessages(@PathVariable Long callId) {
        return service.getMessagesByCall(callId);
    }

    @Operation(summary = "Enviar un mensaje en una visita")
    @ApiResponse(responseCode = "200", description = "Mensaje enviado")
    @PostMapping
    public Message send(@RequestBody MessageDTO dto) {
        return service.send(dto);
    }
}
