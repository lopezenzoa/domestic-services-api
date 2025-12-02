package com.portfolio.domestic_services.dto;

import com.portfolio.domestic_services.model.States;
import java.time.LocalDateTime;

public class ChatListDTO {
    private Long id;
    private Long otherUserId;
    private String otherUserName;
    private String state;
    private String date;

    public ChatListDTO() {
    }

    // ✅ CONSTRUCTOR CLAVE: Recibe los tipos REALES de la base de datos (States, LocalDateTime)
    // Hibernate usará este constructor automáticamente.
    public ChatListDTO(Long id, Long otherUserId, String firstName, String lastName, States state, LocalDateTime date) {
        this.id = id;
        this.otherUserId = otherUserId;
        this.otherUserName = firstName + " " + lastName;

        // Convertimos a String aquí dentro, NO en la Query
        this.state = state != null ? state.toString() : "";
        this.date = date != null ? date.toString() : "";
    }

    // Getters y Setters
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
}