# Flight Ticket Booking API

A simple REST API for booking flight tickets, built with Spring Boot 3.2.5 and Java 21.

## Run the Application

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn spring-boot:run
```

The server starts at `http://localhost:8080`.

## API

### Create a Booking

```
POST /api/bookings
Content-Type: application/json

{
    "flightNumber": "AI101",
    "passengerName": "Kunal"
}
```

**Success (201 Created):**

Headers: `Location: /api/bookings/{bookingId}`
```json
{
    "bookingId": "d4f7a8b2-...",
    "flightNumber": "AI101",
    "passengerName": "Kunal"
}
```

**Errors:**
| Status | Reason |
|--------|--------|
| 400 | Missing or blank `flightNumber` / `passengerName` |
| 400 | Invalid JSON body |
| 400 | Flight number not found |
| 409 | Flight is fully booked |

### Available Flights

| Flight | Capacity |
|--------|----------|
| AI101  | 2        |
| AI202  | 3        |
| AI303  | 1        |

## Design

- **Controller → Service → Model** — no extra layers (DTOs, mappers, repositories).
- Bookings are stored in a `ConcurrentHashMap<String, List<Booking>>` keyed by flight number — O(1) lookup per flight.
- Concurrent requests are handled with per-flight `ReentrantLock` so different flights don't block each other.
- Input is trimmed and normalized (uppercase) before validation.
- Exceptions are mapped to HTTP responses via a centralized `GlobalExceptionHandler`.

## Known Limitations

- All data is lost on restart (in-memory storage).
- Flights and capacities are hardcoded in `BookingService`.
- No GET endpoint to retrieve bookings.
- No update or cancel functionality.

## What I'd Do With More Time

These are things I intentionally left out to keep the scope tight, but would naturally come next:

- Switch to a real database — H2 is already in `pom.xml`, just needs entity annotations and a repository.
- Add a GET endpoint so you can actually see what's been booked without restarting.
- Let users cancel bookings (DELETE) and free up seats.
- Move hardcoded flight data into `application.properties` or a database table so it's not buried in code.
- Add basic logging to trace what's happening when things go wrong in production.
- Write a few integration tests — especially around the concurrency logic, since that's the trickiest part to trust without proof.
