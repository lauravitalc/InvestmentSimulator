package com.testetecnico.investmentsimulator.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulation_result")
public class SimulationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @OneToOne
    @JoinColumn(name = "simulation_id", nullable = false)
    private InvestmentSimulation simulation;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal finalValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalInvested;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalReturn;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal estimatedTax;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal netValue;
}
