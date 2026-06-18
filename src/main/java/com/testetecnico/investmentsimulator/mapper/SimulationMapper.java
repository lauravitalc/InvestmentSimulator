package com.testetecnico.investmentsimulator.mapper;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;
import com.testetecnico.investmentsimulator.dto.SimulationResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SimulationMapper {
    public SimulationResponseDTO toResponseDTO(InvestmentSimulation simulation) {
        SimulationResult result = simulation.getResult();

        return new SimulationResponseDTO(
                simulation.getId(),
                simulation.getNameInvestor(),
                simulation.getInvestmentType(),
                simulation.getInitialContribution(),
                simulation.getMonthlyContribution(),
                simulation.getTimeMonths(),
                simulation.getAnnualRate(),
                simulation.getCreatedAt(),
                result.getFinalValue(),
                result.getTotalInvested(),
                result.getTotalReturn(),
                result.getEstimatedTax(),
                result.getNetValue()
        );
    }
}
