package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.service.BookingService;
import gr.hua.dit.studyrooms.core.service.model.CreateBookingRequest;
import gr.hua.dit.studyrooms.web.ui.model.CreateBookingForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Σελίδα με τη λίστα των κρατήσεων (Οι κρατήσεις μου)
    @GetMapping
    @Secured({"ROLE_STUDENT", "ROLE_STAFF", "ROLE_ADMIN"})
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getMyBookings());
        return "bookings";
    }

    // Σελίδα με τη φόρμα νέας κράτησης
    @GetMapping("/new")
    @Secured("ROLE_STUDENT")
    public String newBookingForm(Model model) {
        model.addAttribute("form", new CreateBookingForm());
        model.addAttribute("rooms", bookingService.getAllActiveRooms()); // Για το dropdown
        return "new_booking";
    }

    // --- ADMIN & STAFF VIEW ---

    // Εμφάνιση ΟΛΩΝ των κρατήσεων (Διαχείριση)
    @GetMapping("/all")
    @Secured({"ROLE_ADMIN", "ROLE_STAFF"}) // Προσθήκη του ROLE_STAFF
    public String listAllBookingsForAdmin(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    // Υποβολή της φόρμας νέας κράτησης
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
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", bookingService.getAllActiveRooms());
            return "new_booking";
        }
    }

    // --- ΑΚΥΡΩΣΗ ΚΡΑΤΗΣΗΣ ---

    @PostMapping("/{id}/cancel")
    @Secured({"ROLE_STUDENT", "ROLE_STAFF", "ROLE_ADMIN"})
    public String cancelBooking(@PathVariable Long id, HttpServletRequest request) {
        bookingService.cancelBooking(id);

        // Επιστροφή στην προηγούμενη σελίδα (για να δουλεύει σωστά και για admin/staff και για student)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/bookings");
    }
}
