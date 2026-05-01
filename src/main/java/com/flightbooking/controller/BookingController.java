package com.flightbooking.controller;

import com.flightbooking.model.Booking;
import com.flightbooking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking created = bookingService.createBooking(booking);

        URI location = URI.create("/api/bookings/" + created.getBookingId());

        return ResponseEntity
                .created(location)
                .body(created);
    }
}