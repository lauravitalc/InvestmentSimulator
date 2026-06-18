package com.testetecnico.investmentsimulator.service;

import com.testetecnico.investmentsimulator.dto.SimulationRequestDTO;
import com.testetecnico.investmentsimulator.dto.SimulationResponseDTO;

import java.util.List;
import java.util.UUID;

public interface Simulation {
    SimulationResponseDTO create(SimulationRequestDTO request);
    List<SimulationResponseDTO> findAll();
    SimulationResponseDTO findById(UUID id);
    void delete(UUID id);
}
