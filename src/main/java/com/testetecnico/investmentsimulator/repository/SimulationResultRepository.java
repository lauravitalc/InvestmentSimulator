package com.testetecnico.investmentsimulator.repository;

import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResult, UUID> {
}
