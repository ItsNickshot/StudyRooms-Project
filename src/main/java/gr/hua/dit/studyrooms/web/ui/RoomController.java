package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.service.BookingService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final BookingService bookingService;

    public RoomController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1. Λίστα Αιθουσών (Διαχείριση)
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String listRooms(Model model) {
        model.addAttribute("rooms", bookingService.getAllRooms());
        return "rooms"; // Το νέο HTML αρχείο που θα φτιάξουμε
    }

    // 2. Φόρμα Δημιουργίας
    @GetMapping("/new")
    @Secured("ROLE_ADMIN")
    public String showCreateRoomForm() {
        return "new_room";
    }

    // 3. Αποθήκευση
    @PostMapping("/new")
    @Secured("ROLE_ADMIN")
    public String createRoom(@RequestParam String name, @RequestParam Integer capacity) {
        bookingService.createRoom(name, capacity);
        return "redirect:/rooms"; // Επιστροφή στη λίστα
    }

    // 4. Διαγραφή
    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteRoom(@PathVariable Long id) {
        try {
            bookingService.deleteRoom(id);
        } catch (Exception e) {
            // Αν έχει κρατήσεις, μπορεί να αποτύχει. Για το demo δεν πειράζει.
            return "redirect:/rooms?error=true";
        }
        return "redirect:/rooms";
    }

    // 5. Εμφάνιση Φόρμας Επεξεργασίας
    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", bookingService.getRoomById(id));
        return "edit_room"; // Θα φτιάξουμε αυτό το αρχείο τώρα
    }

    // 6. Αποθήκευση Αλλαγών
    @PostMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String updateRoom(@PathVariable Long id,
                             @RequestParam String name,
                             @RequestParam Integer capacity,
                             @RequestParam(required = false, defaultValue = "false") Boolean active) {
        bookingService.updateRoom(id, name, capacity, active);
        return "redirect:/rooms";
    }
}
