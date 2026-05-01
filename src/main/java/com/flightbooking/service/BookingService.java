package com.flightbooking.service;

import com.flightbooking.model.Booking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final List<Booking> bookings = new ArrayList<>();

    public Booking createBooking(Booking booking) {
        booking.setBookingId(UUID.randomUUID().toString());
        bookings.add(booking);
        return booking;
    }
}
