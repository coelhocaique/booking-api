package com.coelhocaique.booking.unit;

import com.coelhocaique.booking.dto.BookingDTO;
import com.coelhocaique.booking.dto.CreateBookingRequestDTO;
import com.coelhocaique.booking.dto.UpdateBookingRequestDTO;
import com.coelhocaique.booking.entity.Booking;
import com.coelhocaique.booking.entity.BookingStatus;
import com.coelhocaique.booking.exception.ValidationException;
import com.coelhocaique.booking.repository.BookingRepository;
import com.coelhocaique.booking.service.BookingService;
import com.coelhocaique.booking.service.PropertyBlockService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private PropertyBlockService propertyBlockService;

    @InjectMocks
    private BookingService service;

    private final Long PROPERTY_ID = 1L;
    private final Long BOOKING_ID = 2L;

    @Test
    public void testCreate() {
        CreateBookingRequestDTO createBookingRequest = createBookingRequest();
        LocalDate endDate = createBookingRequest.getEndDate();
        LocalDate startDate = createBookingRequest.getStartDate();

        when(repository.save(any(Booking.class))).thenAnswer(it -> {
            Booking booking = it.getArgument(0);
            booking.setId(BOOKING_ID);
            return booking;
        });
        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(Collections.emptyList());

        when(propertyBlockService.isPropertyBlocked(PROPERTY_ID, startDate, endDate)).thenReturn(false);

        BookingDTO result = service.create(createBookingRequest);

        assertNotNull(result);
        assertEquals(BOOKING_ID, result.getId());
        assertEquals(PROPERTY_ID, result.getPropertyId());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        assertEquals(createBookingRequest.getGuestInfo(), result.getGuestInfo());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());

        verify(repository).save(any(Booking.class));
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }

    @Test
    public void testCreateWhenDatePeriodInvalid() {
        LocalDate startDate = LocalDate.of(2024, 6, 19);
        LocalDate endDate = LocalDate.of(2024, 6, 18);
        CreateBookingRequestDTO createBookingRequest = createBookingRequest(startDate, endDate, "Coelho Caique");

        assertThrows(ValidationException.class, () -> service.create(createBookingRequest));

        verify(repository, never()).save(any(Booking.class));
        verify(repository, never()).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService, never()).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }

    @Test
    public void testCreateWhenAlreadyBooked() {
        LocalDate startDate = LocalDate.of(2024, 6, 19);
        LocalDate endDate = LocalDate.of(2024, 6, 19);
        CreateBookingRequestDTO createBookingRequest = createBookingRequest(startDate, endDate, "Coelho Caique");

        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(List.of(createEntity()));

        assertThrows(ValidationException.class, () -> service.create(createBookingRequest));

        verify(repository, never()).save(any(Booking.class));
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService, never()).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }

    @Test
    public void testCreateWhenPropertyIsBlocked() {
        LocalDate startDate = LocalDate.of(2024, 6, 19);
        LocalDate endDate = LocalDate.of(2024, 6, 19);
        CreateBookingRequestDTO createBookingRequest = createBookingRequest(startDate, endDate, "Coelho Caique");

        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(List.of());

        when(propertyBlockService.isPropertyBlocked(PROPERTY_ID, startDate, endDate)).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.create(createBookingRequest));

        verify(repository, never()).save(any(Booking.class));
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }

    @Test
    public void testUpdate() {
        LocalDate endDate = LocalDate.of(2024, 6, 17);
        LocalDate startDate = LocalDate.of(2024, 6, 15);
        UpdateBookingRequestDTO updateBookingRequest = updateBookingRequest(startDate, endDate, "Coelho Caique");

        when(repository.save(any(Booking.class))).thenAnswer(it -> it.getArgument(0));

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(createEntity()));

        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(Collections.emptyList());

        when(propertyBlockService.isPropertyBlocked(PROPERTY_ID, startDate, endDate)).thenReturn(false);

        BookingDTO result = service.update(BOOKING_ID, updateBookingRequest);

        assertNotNull(result);
        assertEquals(BOOKING_ID, result.getId());
        assertEquals(PROPERTY_ID, result.getPropertyId());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        assertEquals(updateBookingRequest.getGuestInfo(), result.getGuestInfo());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());

        verify(repository).save(any(Booking.class));
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testDelete() {
        Booking booking = createEntity();
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        service.delete(BOOKING_ID);

        verify(repository).delete(booking);
        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testDeleteWhenBookingNotFound() {
        Booking booking = createEntity();
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.delete(BOOKING_ID));

        verify(repository, times(0)).delete(booking);
        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testFindById() {
        Booking booking = createEntity();
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));

        BookingDTO result = service.findById(BOOKING_ID);

        assertNotNull(result);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getPropertyId(), result.getPropertyId());
        assertEquals(booking.getStartDate(), result.getStartDate());
        assertEquals(booking.getEndDate(), result.getEndDate());
        assertEquals(booking.getStatus(), result.getStatus());

        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testFindByIdWhenDoesNotExist() {
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.findById(BOOKING_ID));

        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testCancel() {
        Booking booking = createEntity();
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.save(any(Booking.class))).thenAnswer(it -> it.getArgument(0));

        service.cancel(BOOKING_ID);

        verify(repository).save(booking);
        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testCancelWhenDoesNotExist() {
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.cancel(BOOKING_ID));

        verify(repository).findById(BOOKING_ID);
    }

    @Test
    public void testRebook() {
        Booking booking = createEntity();
        booking.setStatus(BookingStatus.CANCELED);
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();

        when(repository.save(any(Booking.class))).thenAnswer(it -> it.getArgument(0));
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(Collections.emptyList());
        when(propertyBlockService.isPropertyBlocked(PROPERTY_ID, startDate, endDate)).thenReturn(false);

        service.rebook(BOOKING_ID);
        booking.setStatus(BookingStatus.REBOOKED);

        verify(repository).save(booking);
        verify(repository).findById(BOOKING_ID);
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }

    @Test
    public void testRebookWhenBookingNotFound() {
        when(repository.findById(BOOKING_ID)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.rebook(BOOKING_ID));

        verify(repository, never()).save(any());
        verify(repository).findById(BOOKING_ID);
        verify(repository, never()).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(any(), any(), any(), any());
        verify(propertyBlockService, never()).isPropertyBlocked(anyLong(), any(), any());
    }

    @Test
    public void testRebookWhenInvalidDatePeriod() {
        Booking booking = createEntity();
        booking.setStatus(BookingStatus.CANCELED);

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, booking.getEndDate(), booking.getStartDate())).thenReturn(List.of(booking));

        assertThrows(ValidationException.class, () -> service.rebook(BOOKING_ID));

        verify(repository, never()).save(booking);
        verify(repository).findById(BOOKING_ID);
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, booking.getEndDate(), booking.getStartDate());
        verify(propertyBlockService, never()).isPropertyBlocked(anyLong(), any(), any());
    }

    @Test
    public void testRebookWhenPropertyIsBlocked() {
        Booking booking = createEntity();
        booking.setStatus(BookingStatus.CANCELED);
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();

        when(repository.findById(BOOKING_ID)).thenReturn(Optional.of(booking));
        when(repository.findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED,
                PROPERTY_ID, endDate, startDate)).thenReturn(List.of());
        when(propertyBlockService.isPropertyBlocked(PROPERTY_ID, startDate, endDate)).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.rebook(BOOKING_ID));

        verify(repository, never()).save(booking);
        verify(repository).findById(BOOKING_ID);
        verify(repository).findByStatusNotAndPropertyIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(BookingStatus.CANCELED, PROPERTY_ID, endDate, startDate);
        verify(propertyBlockService).isPropertyBlocked(PROPERTY_ID, startDate, endDate);
    }


    private Booking createEntity() {
        return Booking.builder()
                .id(BOOKING_ID)
                .propertyId(PROPERTY_ID)
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2024, 6, 10))
                .guestInfo("Caique Coelho")
                .status(BookingStatus.ACTIVE)
                .build();
    }

    private CreateBookingRequestDTO createBookingRequest() {
        return createBookingRequest(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 10), "Caique Coelho");
    }

    private CreateBookingRequestDTO createBookingRequest(LocalDate startDate, LocalDate endDate, String guestInfo) {
        return CreateBookingRequestDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .propertyId(PROPERTY_ID)
                .guestInfo(guestInfo)
                .build();
    }

    private UpdateBookingRequestDTO updateBookingRequest(LocalDate startDate, LocalDate endDate, String guestInfo) {
        return UpdateBookingRequestDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .guestInfo(guestInfo)
                .build();
    }

}