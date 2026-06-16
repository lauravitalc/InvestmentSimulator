package com.testetecnico.investmentsimulator.dto;

import com.testetecnico.investmentsimulator.domain.enums.InvestmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResponseDTO {
    private UUID id;
    private String investorName;
    private InvestmentType investmentType;
    private BigDecimal initialContribution;
    private BigDecimal monthlyContribution;
    private Integer timeMonths;
    private BigDecimal annualRate;
    private LocalDateTime createdAt;

    private BigDecimal finalValue;
    private BigDecimal totalInvested;
    private BigDecimal totalReturn;
    private BigDecimal estimatedTax;
    private BigDecimal netValue;
}
