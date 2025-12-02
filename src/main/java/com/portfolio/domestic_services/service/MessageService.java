package com.portfolio.domestic_services.service;

import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByCall(Long callId);
    List<Call> getChatsByUser(Long userId);

    Message send(MessageDTO dto);
}
