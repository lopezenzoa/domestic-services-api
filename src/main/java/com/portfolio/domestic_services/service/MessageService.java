package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByCall(Long callId);

    Message send(MessageDTO dto);
    List<ChatListDTO> getChatsByUser(Long userId);
    void markMessagesAsSeen(Long callId, Long userId);
}
