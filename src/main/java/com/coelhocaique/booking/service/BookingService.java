package com.coelhocaique.booking.service;

import com.coelhocaique.booking.dto.BookingDTO;
import com.coelhocaique.booking.dto.CreateBookingRequestDTO;
import com.coelhocaique.booking.dto.UpdateBookingRequestDTO;
import com.coelhocaique.booking.entity.Booking;
import com.coelhocaique.booking.entity.BookingStatus;
import com.coelhocaique.booking.exception.ValidationException;
import com.coelhocaique.booking.mapper.BookingMapper;
import com.coelhocaique.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;

    private final PropertyBlockService propertyBlockService;
    
    public Optional<BookingDTO> findById(Long id) {
        return repository.findById(id)
                .map(BookingMapper::toDTO);
    }
    public BookingDTO create(CreateBookingRequestDTO createBookingRequest) {
        Booking booking = BookingMapper.toEntity(createBookingRequest);
        validateOverlappingDates(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId());
        return BookingMapper.toDTO(repository.save(booking));
    }

    public BookingDTO update(Long id, UpdateBookingRequestDTO updateBookingRequest) {
        Booking booking = findOne(id);
        if (nonNull(updateBookingRequest.getGuestInfo())){
            booking.setGuestInfo(updateBookingRequest.getGuestInfo());
        }
        if (nonNull(updateBookingRequest.getStartDate()) && nonNull(updateBookingRequest.getEndDate())) {
            booking.setStartDate(updateBookingRequest.getStartDate());
            booking.setEndDate(updateBookingRequest.getEndDate());
        }

        return BookingMapper.toDTO(repository.save(booking));
    }

    public void cancel(Long id) {
        Booking booking = findOne(id);
        booking.setStatus(BookingStatus.CANCELED);
        repository.save(booking);
    }

    public BookingDTO rebook(Long id) {
        Booking booking = findOne(id);
        validateOverlappingDates(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId());
        booking.setStatus(BookingStatus.REBOOKED);
        return BookingMapper.toDTO(repository.save(booking));
    }

    public void delete(Long id) {
        Booking booking = findOne(id);
        repository.delete(booking);
    }

    private Booking findOne(Long id) {
        return repository.findById(id).orElseThrow(ValidationException::bookingNotFound);
    }

    private void validateOverlappingDates(LocalDate startDate, LocalDate endDate, Long propertyId) {
        if (endDate.isBefore(startDate)) {
            throw ValidationException.invalidDateInterval();
        }

        List<Booking> bookings = repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                BookingStatus.CANCELED,
                propertyId,
                endDate,
                startDate);

        if (bookings.size() > 0) {
            throw ValidationException.propertyAlreadyBooked();
        }

        if (propertyBlockService.isPropertyBlocked(propertyId, startDate, endDate)) {
            throw ValidationException.propertyBlocked();
        }
    }
}
