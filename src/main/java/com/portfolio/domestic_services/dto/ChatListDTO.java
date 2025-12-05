package com.portfolio.domestic_services.dto;

import com.portfolio.domestic_services.model.States;
import java.time.LocalDateTime;

public class ChatListDTO {

    private Long id;                // ID de la Call
    private Long otherUserId;
    private String otherUserName;
    private String state;
    private String date;
    private long unreadCount;

    private String lastMessage;
    private String lastMessageTime;

    public ChatListDTO() {}

    // Constructor usado por Hibernate desde el Query original
    public ChatListDTO(Long id, Long otherUserId, String firstName, String lastName,
                       States state, LocalDateTime date) {

        this.id = id;
        this.otherUserId = otherUserId;
        this.otherUserName = firstName + " " + lastName;
        this.state = state != null ? state.toString() : "";
        this.date = date != null ? date.toString() : "";
    }

    // Constructor COMPLETO: usado cuando sumamos lastMessage
    public ChatListDTO(Long id, Long otherUserId, String firstName, String lastName,
                       String state, LocalDateTime date,
                       String lastMessage, String lastMessageTime) {

        this.id = id;  // ✔️ ANTES ESTABA MAL!
        this.otherUserId = otherUserId;
        this.otherUserName = firstName + " " + lastName;
        this.state = state;
        this.date = date != null ? date.toString() : null;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOtherUserId() { return otherUserId; }
    public void setOtherUserId(Long otherUserId) { this.otherUserId = otherUserId; }

    public String getOtherUserName() { return otherUserName; }
    public void setOtherUserName(String otherUserName) { this.otherUserName = otherUserName; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public long getUnreadCount() { return unreadCount; }
    public void setUnreadCount(long unreadCount) { this.unreadCount = unreadCount; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }
}
