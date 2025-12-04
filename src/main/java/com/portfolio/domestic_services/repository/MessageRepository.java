package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {
    long countByCallIdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId); // Cuenta mensajes de una llamada específica, que NO envié yo y que NO están vistos
    List<Message> findByCallIdOrderByTimestampAsc(Long callId);
    List<Message> findByAuthorId(Long authorId);
    List<Message> findByCallIdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId);

    long countByCall_IdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId);
    List<Message> findByCall_IdOrderByTimestampAsc(Long callId);
    List<Message> findByCall_IdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId);


}
