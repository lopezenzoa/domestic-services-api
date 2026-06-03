package com.portfolio.domestic_services.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean seen;

    @ManyToOne
    @JoinColumn(name = "call_id", nullable = false)
    private Call call;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
