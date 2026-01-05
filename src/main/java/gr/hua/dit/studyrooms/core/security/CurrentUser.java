package gr.hua.dit.studyrooms.core.security;

import gr.hua.dit.studyrooms.core.model.Person;

public class CurrentUser {
    private final ApplicationUserDetails userDetails;

    public CurrentUser(ApplicationUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getUsername() {
        return userDetails.getUsername();
    }

    public Person getPerson() {
        return userDetails.getPerson();
    }
}
