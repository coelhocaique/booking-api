package com.coelhocaique.booking.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ErrorDTO {

    private final List<String> errors;

    public ErrorDTO(String error) {
        this.errors = List.of(error);
    }
}