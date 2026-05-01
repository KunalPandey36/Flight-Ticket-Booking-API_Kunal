package com.flightbooking.service;

import com.flightbooking.model.Booking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookingService {

    private final List<Booking> bookings = new ArrayList<>();

    private final Map<String, Integer> flightCapacities = Map.of(
        "AI101", 2,
        "AI202", 3,
        "AI303", 1
    );

    public Booking createBooking(Booking booking) {
        String flightNumber = booking.getFlightNumber();

        if (!flightCapacities.containsKey(flightNumber)) {
            throw new IllegalArgumentException("Flight " + flightNumber + " not found");
        }

        long bookedCount = bookings.stream()
            .filter(b -> b.getFlightNumber().equals(flightNumber))
            .count();

        if (bookedCount >= flightCapacities.get(flightNumber)) {
            throw new IllegalStateException("Flight " + flightNumber + " is fully booked");
        }

        booking.setBookingId(UUID.randomUUID().toString());
        bookings.add(booking);
        return booking;
    }
}
