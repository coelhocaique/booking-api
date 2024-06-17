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
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDTO> patch(@PathVariable Long id, @RequestBody UpdateBookingRequestDTO updateBookingRequest) {
        BookingDTO bookingDTO = bookingService.update(id, updateBookingRequest);
        return ResponseEntity.ok(bookingDTO);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/rebook")
    public ResponseEntity<BookingDTO> rebook(@PathVariable Long id) {
        BookingDTO rebookedBooking = bookingService.rebook(id);
        return ResponseEntity.ok(rebookedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }
}
