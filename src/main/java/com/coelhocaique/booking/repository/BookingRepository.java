package com.coelhocaique.booking.repository;

import com.coelhocaique.booking.entity.Booking;
import com.coelhocaique.booking.entity.BookingStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus status, Long propertyId, LocalDate endDate, LocalDate startDate);
}
