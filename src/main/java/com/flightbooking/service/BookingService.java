package com.flightbooking.service;

import com.flightbooking.model.Booking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookingService {

    private final List<Booking> bookings = new CopyOnWriteArrayList<>();

    private final Map<String, Integer> flightCapacities = Map.of(
        "AI101", 2,
        "AI202", 3,
        "AI303", 1
    );

    private final ConcurrentHashMap<String, ReentrantLock> flightLocks = new ConcurrentHashMap<>();

    public Booking createBooking(Booking booking) {
        validateInput(booking);

        String flightNumber = booking.getFlightNumber().trim().toUpperCase();
        booking.setFlightNumber(flightNumber);

        if (!flightCapacities.containsKey(flightNumber)) {
            throw new IllegalArgumentException("Flight " + flightNumber + " not found");
        }

        ReentrantLock lock = flightLocks.computeIfAbsent(flightNumber, k -> new ReentrantLock());
        lock.lock();
        try {
            long bookedCount = bookings.stream()
                .filter(b -> b.getFlightNumber().equals(flightNumber))
                .count();

            if (bookedCount >= flightCapacities.get(flightNumber)) {
                throw new IllegalStateException("Flight " + flightNumber + " is fully booked");
            }

            booking.setBookingId(UUID.randomUUID().toString());
            bookings.add(booking);
            return booking;
        } finally {
            lock.unlock();
        }
    }

    private void validateInput(Booking booking) {
        if (booking.getFlightNumber() == null || booking.getFlightNumber().isBlank()) {
            throw new IllegalArgumentException("Flight number is required");
        }
        if (booking.getPassengerName() == null || booking.getPassengerName().isBlank()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
    }
}
