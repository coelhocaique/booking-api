package com.coelhocaique.booking.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorDTO {

    private final String message;
}
