package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.service.BookingService;
import gr.hua.dit.studyrooms.core.service.model.CreateBookingRequest;
import gr.hua.dit.studyrooms.web.ui.model.CreateBookingForm;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Σελίδα με τη λίστα των κρατήσεων
    @GetMapping
    @Secured({"ROLE_STUDENT", "ROLE_STAFF", "ROLE_ADMIN"})
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getMyBookings());
        return "bookings"; // Θα φτιάξουμε το bookings.html μετά
    }

    // Σελίδα με τη φόρμα νέας κράτησης
    @GetMapping("/new")
    @Secured("ROLE_STUDENT")
    public String newBookingForm(Model model) {
        model.addAttribute("form", new CreateBookingForm());
        model.addAttribute("rooms", bookingService.getAllActiveRooms()); // Για το dropdown
        return "new_booking"; // Θα φτιάξουμε το new_booking.html μετά
    }

    // --- ΠΡΟΣΘΗΚΗ ΓΙΑ ADMIN ---

    // Αυτό το URL θα είναι: localhost:8080/bookings/all
    @GetMapping("/all")
    @Secured("ROLE_ADMIN") // Μόνο ο Admin μπορεί να το δει
    public String listAllBookingsForAdmin(Model model) {
        // Εδώ καλούμε τη μέθοδο που φέρνει ΤΑ ΠΑΝΤΑ (όχι μόνο τα δικά του)
        model.addAttribute("bookings", bookingService.getAllBookings());

        // Μπορούμε να χρησιμοποιήσουμε το ίδιο HTML αρχείο (bookings.html)
        return "bookings";
    }

    // Υποβολή της φόρμας
    @PostMapping("/new")
    @Secured("ROLE_STUDENT")
    public String createBooking(@Valid @ModelAttribute("form") CreateBookingForm form,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", bookingService.getAllActiveRooms());
            return "new_booking";
        }

        try {
            CreateBookingRequest request = new CreateBookingRequest(
                form.getRoomId(),
                form.getStartTime(),
                form.getEndTime()
            );
            bookingService.createBooking(request);
            return "redirect:/bookings"; // Επιτυχία -> Πήγαινε στη λίστα
        } catch (Exception e) {
            // Αν πετάξει λάθος (π.χ. Αργία ή Επικάλυψη), το δείχνουμε στη φόρμα
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", bookingService.getAllActiveRooms());
            return "new_booking";
        }
    }
}
