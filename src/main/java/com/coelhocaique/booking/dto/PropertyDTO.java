package com.coelhocaique.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PropertyDTO {

    private final Long id;

    private final String name;

    private final String address;
}
