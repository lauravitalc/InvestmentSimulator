package com.testetecnico.investmentsimulator.controller;

import com.testetecnico.investmentsimulator.dto.SimulationRequestDTO;
import com.testetecnico.investmentsimulator.dto.SimulationResponseDTO;
import com.testetecnico.investmentsimulator.service.SimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/simulations")
@RequiredArgsConstructor
public class SimulationController {
    private final SimulationService _simulationService;

    @PostMapping
    public ResponseEntity<SimulationResponseDTO> create(@RequestBody @Valid SimulationRequestDTO request) {
        SimulationResponseDTO response = _simulationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SimulationResponseDTO>> findAll() {
        List<SimulationResponseDTO> simulations = _simulationService.findAll();
        return ResponseEntity.ok(simulations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationResponseDTO> findById(@PathVariable UUID id) {
        SimulationResponseDTO simulation = _simulationService.findById(id);
        return ResponseEntity.ok(simulation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        _simulationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
