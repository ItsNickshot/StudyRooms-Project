package gr.hua.dit.studyrooms.core.security;

import gr.hua.dit.studyrooms.core.model.Person;
import gr.hua.dit.studyrooms.core.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public ApplicationUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return new ApplicationUserDetails(person);
    }
}
