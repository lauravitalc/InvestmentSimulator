package com.testetecnico.investmentsimulator.service.impl;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;
import com.testetecnico.investmentsimulator.service.SimulationCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SimulationCalculatorService implements SimulationCalculator {
    public SimulationResult calculate(InvestmentSimulation simulation) {
        BigDecimal monthlyRate = calculateMonthlyRate(simulation);
        BigDecimal initial = calculateInitialContribution(simulation);

        BigDecimal finalValue = initial;
        finalValue = calculateFinalValue(simulation, finalValue, monthlyRate);

        BigDecimal totalInvested = initial
                .add(simulation.getMonthlyContribution()
                    .multiply(BigDecimal.valueOf(simulation.getTimeMonths())))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalReturn = finalValue.subtract(totalInvested);
        BigDecimal estimatedTax = totalReturn.multiply(taxRate(simulation.getTimeMonths()));
        BigDecimal netValue = finalValue.subtract(estimatedTax).setScale(2, RoundingMode.HALF_UP);

        return buildResult(simulation, finalValue, totalInvested, totalReturn, estimatedTax, netValue);
    }

    private BigDecimal calculateMonthlyRate(InvestmentSimulation simulation) {
        return simulation.getAnnualRate()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInitialContribution(InvestmentSimulation simulation) {
        return simulation.getInitialContribution() != null
                ? simulation.getInitialContribution()
                : BigDecimal.ZERO;
    }

    private BigDecimal calculateFinalValue(InvestmentSimulation simulation, BigDecimal finalValue,BigDecimal monthlyRate) {
        for (int month = 1; month <= simulation.getTimeMonths(); month++) {
            finalValue = finalValue
                    .multiply(BigDecimal.ONE.add(monthlyRate))
                    .add(simulation.getMonthlyContribution());
        }

        finalValue = finalValue.setScale(2, RoundingMode.HALF_UP);

        return finalValue;
    }

    private SimulationResult buildResult(InvestmentSimulation simulation, BigDecimal finalValue, BigDecimal totalInvested, BigDecimal totalReturn, BigDecimal estimatedTax, BigDecimal netValue) {
        var result = new SimulationResult();
        result.setSimulation(simulation);
        result.setFinalValue(finalValue);
        result.setTotalInvested(totalInvested);
        result.setTotalReturn(totalReturn.setScale(2, RoundingMode.HALF_UP));
        result.setEstimatedTax(estimatedTax.setScale(2, RoundingMode.HALF_UP));
        result.setNetValue(netValue);

        return result;
    }

    private BigDecimal taxRate(int months) {
        if (months <= 6)  return new BigDecimal("0.225");
        if (months <= 12) return new BigDecimal("0.200");
        if (months <= 24) return new BigDecimal("0.175");
        return new BigDecimal("0.150");
    }
}
