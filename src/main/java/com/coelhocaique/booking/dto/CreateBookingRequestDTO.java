package com.coelhocaique.booking.dto;

import com.coelhocaique.booking.validator.PropertyId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class CreateBookingRequestDTO {

    @PropertyId
    private Long propertyId;

    @NotEmpty
    private String guestInfo;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
