package com.portfolio.domestic_services.repository;

import com.portfolio.domestic_services.model.Call;
import com.portfolio.domestic_services.model.States;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findAllByClientId(Long id);
    List<Call> findAllByProviderId(Long id);
    List<Call> findAllByStateAndProviderId(States state, Long id);
}
