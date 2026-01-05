package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.security.CurrentUser;
import gr.hua.dit.studyrooms.core.security.CurrentUserProvider;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserControllerAdvice {

    private final CurrentUserProvider currentUserProvider;

    public CurrentUserControllerAdvice(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @ModelAttribute
    public void addCurrentUser(Model model) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
        }
    }
}
