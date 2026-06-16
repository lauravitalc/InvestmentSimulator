package com.testetecnico.investmentsimulator.repository;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvestmentSimulationRepository extends JpaRepository<InvestmentSimulation, UUID> {
}
