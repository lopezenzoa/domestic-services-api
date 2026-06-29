package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.dto.ChatListDTO;
import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.shared.enums.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findAllByClientId(Long id);
    List<Call> findAllByProviderId(Long id);
    List<Call> findAllByStateAndProviderId(States state, Long id);

    Page<Call> findAllByProviderId(Long id, Pageable pageable);
    Page<Call> findByProviderIdAndStateIn(Long providerId, List<States> states, Pageable pageable);
    @Query("SELECT c FROM Call c WHERE c.client.id = :userId OR c.provider.id = :userId")
    List<Call> findCallsByUserId(@Param("userId") Long userId);
    @Query("""
    SELECT new com.portfolio.domestic_services.dto.ChatListDTO(
        c.id,
        c.provider.id,
        c.provider.firstName,
        c.provider.lastName,
        c.state,
        c.date
    )
    FROM Call c
    WHERE c.client.id = :clientId
    ORDER BY c.date DESC
    """)
    List<ChatListDTO> findChatsByClientId(@Param("clientId") Long clientId);

    // ✅ CONSULTA ESPEJO PARA PROVEEDOR
    @Query("""
    SELECT new com.portfolio.domestic_services.dto.ChatListDTO(
        c.id,
        c.client.id,
        c.client.firstName,
        c.client.lastName,
        c.state,
        c.date
    )
    FROM Call c
    WHERE c.provider.id = :providerId
    ORDER BY c.date DESC
    """)
    List<ChatListDTO> findChatsByProviderId(@Param("providerId") Long providerId);

    Page<Call> findByProviderIdAndState(Long providerId, States state, Pageable pageable);

    Page<Call> findByProviderIdAndDateBetween(
            Long providerId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<Call> findByProviderIdAndStateAndDateBetween(
            Long providerId,
            States state,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}

