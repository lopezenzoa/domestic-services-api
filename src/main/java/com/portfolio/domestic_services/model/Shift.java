package com.portfolio.domestic_services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shifts")
// This "Shift" entity is meant to be the disposability of a provider
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", nullable = false)
    private Long id;

    @Column(name = "date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime dateTime;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
}
