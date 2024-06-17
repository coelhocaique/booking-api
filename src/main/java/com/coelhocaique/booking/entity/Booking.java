package com.coelhocaique.booking.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String guestInfo;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
