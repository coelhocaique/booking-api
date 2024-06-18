package com.coelhocaique.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class UpdateBookingRequestDTO {

    private String guestInfo;

    @NotNull(message = "Start Date cannot be null.")
    private LocalDate startDate;

    @NotNull(message = "End Date cannot be null.")
    private LocalDate endDate;
}
