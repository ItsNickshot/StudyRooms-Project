package gr.hua.dit.studyrooms.core.service.mapper;

import gr.hua.dit.studyrooms.core.model.Person;
import gr.hua.dit.studyrooms.core.service.model.PersonView;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonView toView(Person person) {
        // Χρησιμοποιούμε username και email που υπάρχουν σίγουρα
        return new PersonView(
            person.getId(),
            person.getUsername(),
            person.getFirstName(),
            person.getLastName(),
            person.getEmail(),
            person.getType().name()
        );
    }
}
