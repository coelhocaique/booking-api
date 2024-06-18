package com.coelhocaique.booking.controller;

import com.coelhocaique.booking.dto.BookingDTO;
import com.coelhocaique.booking.dto.CreateBookingRequestDTO;
import com.coelhocaique.booking.dto.UpdateBookingRequestDTO;
import com.coelhocaique.booking.service.BookingService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> create(@RequestBody @Validated @Valid CreateBookingRequestDTO dto) {
        BookingDTO createdBooking = bookingService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findById(@PathVariable Long id) {
        BookingDTO booking = bookingService.findById(id);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> update(@PathVariable Long id, @RequestBody UpdateBookingRequestDTO updateBookingRequest) {
        BookingDTO bookingDTO = bookingService.update(id, updateBookingRequest);
        return ResponseEntity.ok(bookingDTO);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO> cancel(@PathVariable Long id) {
        BookingDTO cancelledBooking = bookingService.cancel(id);
        return ResponseEntity.ok(cancelledBooking);
    }

    @PatchMapping("/{id}/rebook")
    public ResponseEntity<BookingDTO> rebook(@PathVariable Long id) {
        BookingDTO rebookedBooking = bookingService.rebook(id);
        return ResponseEntity.ok(rebookedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
