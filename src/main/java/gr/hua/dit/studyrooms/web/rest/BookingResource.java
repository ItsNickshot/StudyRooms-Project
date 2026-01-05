package gr.hua.dit.studyrooms.web.rest;

import gr.hua.dit.studyrooms.core.service.BookingService;
import gr.hua.dit.studyrooms.core.service.model.BookingView;
import gr.hua.dit.studyrooms.core.service.model.CreateBookingRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingResource {

    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // GET /api/bookings -> Επιστρέφει τις κρατήσεις του χρήστη
    @GetMapping
    @Secured({"ROLE_STUDENT", "ROLE_STAFF", "ROLE_ADMIN"})
    public ResponseEntity<List<BookingView>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    // POST /api/bookings -> Δημιουργεί νέα κράτηση
    @PostMapping
    @Secured("ROLE_STUDENT")
    public ResponseEntity<BookingView> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        BookingView view = bookingService.createBooking(request);
        return ResponseEntity.created(URI.create("/api/bookings/" + view.id())).body(view);
    }
}
