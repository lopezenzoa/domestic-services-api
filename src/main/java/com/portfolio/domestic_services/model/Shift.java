package com.portfolio.domestic_services.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "shifts")
// This "Shift" entity is meant to be the disposability of a provider
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", columnDefinition = "DATETIME", nullable = false, unique = true)
    private LocalDateTime dateTime;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
}
