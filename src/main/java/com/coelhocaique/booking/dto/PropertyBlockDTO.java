package com.coelhocaique.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PropertyBlockDTO {

    private final Long id;

    private final Long propertyId;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final String reason;
}
