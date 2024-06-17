package com.coelhocaique.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Data
public class UpdateBookingRequestDTO {

    private String guestInfo;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
