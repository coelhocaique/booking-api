package com.coelhocaique.booking.mapper;

import com.coelhocaique.booking.dto.BookingDTO;
import com.coelhocaique.booking.dto.CreateBookingRequestDTO;
import com.coelhocaique.booking.entity.Booking;
import com.coelhocaique.booking.entity.BookingStatus;

public class BookingMapper {

    public static Booking toEntity(CreateBookingRequestDTO createBookingRequest) {
        return Booking.builder()
                .guestInfo(createBookingRequest.getGuestInfo())
                .propertyId(createBookingRequest.getPropertyId())
                .startDate(createBookingRequest.getStartDate())
                .endDate(createBookingRequest.getEndDate())
                .status(BookingStatus.ACTIVE)
                .build();
    }

    public static BookingDTO toDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .guestInfo(booking.getGuestInfo())
                .propertyId(booking.getPropertyId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
