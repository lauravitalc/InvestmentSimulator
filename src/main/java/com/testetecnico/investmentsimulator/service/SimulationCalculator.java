package com.testetecnico.investmentsimulator.service;

import com.testetecnico.investmentsimulator.domain.entity.InvestmentSimulation;
import com.testetecnico.investmentsimulator.domain.entity.SimulationResult;

public interface SimulationCalculator {
    SimulationResult calculate(InvestmentSimulation simulation);
}
