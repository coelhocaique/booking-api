package com.coelhocaique.booking.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyBlockRequestDTO {

    @NotNull(message = "Start Date cannot be null.")
    private LocalDate startDate;

    @NotNull(message = "End Date cannot be null.")
    private LocalDate endDate;

    private String reason;
}
