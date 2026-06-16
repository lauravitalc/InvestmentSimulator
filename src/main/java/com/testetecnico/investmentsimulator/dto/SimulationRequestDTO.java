package com.testetecnico.investmentsimulator.dto;

import com.testetecnico.investmentsimulator.domain.enums.InvestmentType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRequestDTO {
    @NotBlank(message = "Investor name is required")
    private String investorName;

    @NotNull(message = "Investment type is required")
    private InvestmentType investmentType;

    @DecimalMin(value = "0.0", message = "Initial contribution must be positive")
    private BigDecimal initialContribution;

    @NotNull(message = "Monthly contribution is required")
    @Positive(message = "Monthly contribution must be positive")
    private BigDecimal monthlyContribution;

    @NotNull(message = "Time in months is required")
    @Min(value = 1, message = "Minimum period is 1 month")
    @Max(value = 360, message = "Maximum period is 360 months")
    private Integer timeMonths;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.01", message = "Annual rate must be greater than 0")
    private BigDecimal annualRate;
}
