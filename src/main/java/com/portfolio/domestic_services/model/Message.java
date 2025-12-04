package com.portfolio.domestic_services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "call_id")
    @JsonIgnore
    private Call call;
    private Long authorId;
    private String authorRole;
    private String content;
    private LocalDateTime timestamp;
    private boolean seen = false;
    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }

}
