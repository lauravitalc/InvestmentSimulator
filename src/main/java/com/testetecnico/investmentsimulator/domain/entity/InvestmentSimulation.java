package com.testetecnico.investmentsimulator.domain.entity;

import com.testetecnico.investmentsimulator.domain.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investment_simulation")
public class InvestmentSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    private String nameInvestor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType investmentType;

    @Column(precision = 19, scale = 2)
    private BigDecimal initialContribution;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyContribution;

    @Column(nullable = false)
    private Integer timeMonths;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal annualRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "simulation", cascade = CascadeType.ALL)
    private SimulationResult result;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
