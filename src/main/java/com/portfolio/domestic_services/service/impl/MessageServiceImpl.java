package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;
import com.portfolio.domestic_services.repository.CallRepository;
import com.portfolio.domestic_services.repository.MessageRepository;
import com.portfolio.domestic_services.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository repo;

    @Autowired
    private CallRepository callRepo;

    @Override
    public List<Message> getMessagesByCall(Long callId) {
        return repo.findByCallIdOrderByTimestampAsc(callId);
    }

    @Override
    public Message send(MessageDTO dto) {

        Call call = callRepo.findById(dto.getCallId())
                .orElseThrow(() -> new RuntimeException("La visita no existe"));

        Message m = new Message();
        m.setCall(call);
        m.setAuthorId(dto.getAuthorId());
        m.setAuthorRole(dto.getAuthorRole());
        m.setContent(dto.getContent());
        m.setTimestamp(LocalDateTime.now());

        return repo.save(m);
    }
    @Override
    public List<Call> getChatsByUser(Long userId) {
        // Todos los mensajes donde participa el usuario
        List<Message> messages = repo.findByAuthorId(userId);

        // Nos quedamos solo con los call únicos
        return messages.stream()
                .map(Message::getCall)
                .distinct()
                .toList();
    }

}
