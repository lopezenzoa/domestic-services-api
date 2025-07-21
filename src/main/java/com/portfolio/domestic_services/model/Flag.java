package com.portfolio.domestic_services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flags")
@Entity
public class Flag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flag_id")
    private Long id;

    @Column
    private String reason;

    @Column(name = "report_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
}
