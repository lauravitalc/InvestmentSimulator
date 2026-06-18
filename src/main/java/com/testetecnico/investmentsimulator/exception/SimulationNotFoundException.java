package com.testetecnico.investmentsimulator.exception;

import java.util.UUID;

public class SimulationNotFoundException extends RuntimeException {
    public SimulationNotFoundException(UUID id) {
        super("Simulation not found with id: " + id);
    }
}
