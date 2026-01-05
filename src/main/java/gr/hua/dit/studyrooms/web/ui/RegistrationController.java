package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.service.PersonBusinessLogicService;
import gr.hua.dit.studyrooms.core.service.model.CreatePersonRequest;
import gr.hua.dit.studyrooms.web.ui.model.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final PersonBusinessLogicService personService;

    public RegistrationController(PersonBusinessLogicService personService) {
        this.personService = personService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form,
                           BindingResult bindingResult,
                           Model model) {
        // 1. Έλεγχος εγκυρότητας πεδίων (π.χ. κενά πεδία)
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            // 2. Μετατροπή της φόρμας στο Request που θέλει το Service
            CreatePersonRequest request = new CreatePersonRequest(
                form.getUsername(),
                form.getEmail(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastName()
            );

            // 3. Δημιουργία χρήστη
            personService.createPerson(request);

            // 4. Επιτυχία -> Πήγαινε στο Login με μήνυμα επιτυχίας (αν θες)
            return "redirect:/login";

        } catch (Exception e) {
            // 5. Αν το Username/Email υπάρχει ήδη, εμφάνισε το λάθος
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
