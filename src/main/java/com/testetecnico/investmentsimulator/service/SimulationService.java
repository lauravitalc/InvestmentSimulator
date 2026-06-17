package com.testetecnico.investmentsimulator.service;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;
import com.testetecnico.investmentsimulator.dto.SimulationRequestDTO;
import com.testetecnico.investmentsimulator.dto.SimulationResponseDTO;
import com.testetecnico.investmentsimulator.repository.InvestmentSimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimulationService {
    private final InvestmentSimulationRepository _simulationRepository;
    private final SimulationCalculatorService _calculatorService;

    public SimulationResponseDTO create(SimulationRequestDTO request) {
        var simulation = new InvestmentSimulation();
        simulation.setNameInvestor(request.getInvestorName());
        simulation.setInvestmentType(request.getInvestmentType());
        simulation.setInitialContribution(request.getInitialContribution());
        simulation.setMonthlyContribution(request.getMonthlyContribution());
        simulation.setTimeMonths(request.getTimeMonths());
        simulation.setAnnualRate(request.getAnnualRate());

        SimulationResult result = _calculatorService.calculate(simulation);
        simulation.setResult(result);

        InvestmentSimulation saved = _simulationRepository.save(simulation);

        return toResponseDTO(saved);
    }

    public List<SimulationResponseDTO> findAll() {
        return _simulationRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public SimulationResponseDTO findById(UUID id) {
        InvestmentSimulation simulation = _simulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulation not found"));
        return toResponseDTO(simulation);
    }

    public void delete(UUID id) {
        _simulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulation not found"));
        _simulationRepository.deleteById(id);
    }

    private SimulationResponseDTO toResponseDTO(InvestmentSimulation simulation) {
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
