package gr.hua.dit.studyrooms.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(Model model) {
        // Το αντικείμενο 'currentUser' προστίθεται αυτόματα από το CurrentUserControllerAdvice
        // Οπότε δεν χρειάζεται να κάνουμε τίποτα εδώ, απλά επιστρέφουμε το View.
        return "profile";
    }
}
