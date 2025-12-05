package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {

    long countByCall_IdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId);
    List<Message> findByCall_IdOrderByTimestampAsc(Long callId);
    List<Message> findByCall_IdAndAuthorIdNotAndSeenFalse(Long callId, Long myUserId);
    Message findTopByCall_IdOrderByTimestampDesc(Long callId);

}
