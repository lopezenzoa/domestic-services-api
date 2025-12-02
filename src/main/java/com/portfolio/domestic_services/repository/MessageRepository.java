package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByCallIdOrderByTimestampAsc(Long callId);
    List<Message> findByAuthorId(Long authorId);

}
