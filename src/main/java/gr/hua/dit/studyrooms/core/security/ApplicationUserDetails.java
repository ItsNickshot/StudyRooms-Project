package gr.hua.dit.studyrooms.core.security;

import gr.hua.dit.studyrooms.core.model.Person;
import gr.hua.dit.studyrooms.core.model.PersonType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ApplicationUserDetails implements UserDetails {

    private final Person person;

    public ApplicationUserDetails(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Αντιστοίχιση PersonType σε Spring Security Role
        if (person.getType() == PersonType.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (person.getType() == PersonType.STAFF) {
            return List.of(new SimpleGrantedAuthority("ROLE_STAFF"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }
    }

    @Override
    public String getPassword() { return person.getPassword(); }

    @Override
    public String getUsername() { return person.getUsername(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
