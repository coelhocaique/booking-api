package com.coelhocaique.booking.dto;

import com.coelhocaique.booking.validator.PropertyId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateBookingRequestDTO {

    @NotNull(message = "Property ID cannot be null.")
    @PropertyId
    private Long propertyId;

    @NotEmpty(message = "Guest info cannot be empty.")
    private String guestInfo;

    @NotNull(message = "Start Date cannot be null.")
    private LocalDate startDate;

    @NotNull(message = "End Date cannot be null.")
    private LocalDate endDate;
}
