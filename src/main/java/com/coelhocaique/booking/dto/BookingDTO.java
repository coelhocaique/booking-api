package com.coelhocaique.booking.dto;

import com.coelhocaique.booking.entity.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class BookingDTO {

    private final Long id;

    private final Long propertyId;

    private final String guestInfo;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final BookingStatus status;
}
