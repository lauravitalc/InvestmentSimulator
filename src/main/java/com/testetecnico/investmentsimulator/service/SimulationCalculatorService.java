package com.testetecnico.investmentsimulator.service;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SimulationCalculatorService {
    public SimulationResult calculate(InvestmentSimulation simulation) {
        BigDecimal monthlyRate = simulation.getAnnualRate()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal initial = simulation.getInitialContribution() != null
                ? simulation.getInitialContribution()
                : BigDecimal.ZERO;

        BigDecimal finalValue = initial;

        for (int month = 1; month <= simulation.getTimeMonths(); month++) {
            finalValue = finalValue
                    .multiply(BigDecimal.ONE.add(monthlyRate))
                    .add(simulation.getMonthlyContribution());
        }

        finalValue = finalValue.setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalInvested = initial
                .add(simulation.getMonthlyContribution()
                    .multiply(BigDecimal.valueOf(simulation.getTimeMonths())))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalReturn = finalValue.subtract(totalInvested);
        BigDecimal estimatedTax = totalReturn.multiply(taxRate(simulation.getTimeMonths()));
        BigDecimal netValue = finalValue.subtract(estimatedTax).setScale(2, RoundingMode.HALF_UP);

        var result = new SimulationResult();
        result.setSimulation(simulation);
        result.setFinalValue(finalValue);
        result.setTotalInvested(totalInvested);
        result.setTotalReturn(totalReturn.setScale(2, RoundingMode.HALF_UP));
        result.setEstimatedTax(estimatedTax.setScale(2, RoundingMode.HALF_UP));
        result.setNetValue(netValue);

        return result;
    }

    // avaliar passar isso pro banco
    private BigDecimal taxRate(int months) {
        if (months <= 6)  return new BigDecimal("0.225");
        if (months <= 12) return new BigDecimal("0.200");
        if (months <= 24) return new BigDecimal("0.175");
        return new BigDecimal("0.150");
    }
}
