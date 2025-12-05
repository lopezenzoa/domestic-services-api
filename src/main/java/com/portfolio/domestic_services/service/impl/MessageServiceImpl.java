package com.portfolio.domestic_services.service.impl;

import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.dto.MessageDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.Message;
import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.repository.CallRepository;
import com.portfolio.domestic_services.repository.MessageRepository;
import com.portfolio.domestic_services.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        //  CORREGIDO: findByCall_Id... (con guion bajo)
        return repo.findByCall_IdOrderByTimestampAsc(callId);
    }

    @Override
    public List<ChatListDTO> getChatsByUser(Long userId) {

        // 1. Traer todas las calls del usuario
        List<Call> calls = callRepo.findCallsByUserId(userId);

        List<ChatListDTO> result = new ArrayList<>();

        for (Call call : calls) {

            // 2. Traer el último mensaje (si no existe, NO ES CHAT → lo saltamos)
            Message last = repo.findTopByCall_IdOrderByTimestampDesc(call.getId());
            if (last == null) {
                continue; // salteamos visitas sin mensajes → opción B
            }

            // 3. Crear DTO
            ChatListDTO dto = new ChatListDTO();
            dto.setId(call.getId());


            // Quién es el otro usuario según el rol
            User otherUser = call.getClient().getId().equals(userId)
                    ? call.getProvider()
                    : call.getClient();

            dto.setOtherUserName(otherUser.getFirstName() + " " + otherUser.getLastName());

            // 4. Último mensaje
            dto.setLastMessage(last.getContent());
            dto.setLastMessageTime(last.getTimestamp().toString());

            // 5. Mensajes no leídos
            long unread = repo.countByCall_IdAndAuthorIdNotAndSeenFalse(call.getId(), userId);
            dto.setUnreadCount(unread);

            // agregar a la lista final
            result.add(dto);
        }

        return result;
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
        m.setSeen(false);

        return repo.save(m);
    }

    @Override
    public void markMessagesAsSeen(Long callId, Long userId) {
        //  CORREGIDO: findByCall_Id... (con guion bajo)
        List<Message> unreadMessages = repo.findByCall_IdAndAuthorIdNotAndSeenFalse(callId, userId);

        for (Message m : unreadMessages) {
            m.setSeen(true);
        }

        repo.saveAll(unreadMessages);
    }

}