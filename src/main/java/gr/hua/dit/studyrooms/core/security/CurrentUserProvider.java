package gr.hua.dit.studyrooms.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof ApplicationUserDetails) {
            return new CurrentUser((ApplicationUserDetails) principal);
        }
        return null;
    }

    public CurrentUser requireCurrentUser() {
        CurrentUser user = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Current user not found");
        }
        return user;
    }
}
