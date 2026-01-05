package gr.hua.dit.studyrooms.core.service.impl;

import gr.hua.dit.studyrooms.core.model.Person;
import gr.hua.dit.studyrooms.core.model.PersonType;
import gr.hua.dit.studyrooms.core.repository.PersonRepository;
import gr.hua.dit.studyrooms.core.service.PersonBusinessLogicService;
import gr.hua.dit.studyrooms.core.service.model.CreatePersonRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonBusinessLogicServiceImpl implements PersonBusinessLogicService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonBusinessLogicServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long createPerson(CreatePersonRequest request) {
        if (personRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (personRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        Person person = new Person();
        person.setUsername(request.username());
        person.setEmail(request.email());
        person.setPassword(passwordEncoder.encode(request.password()));
        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());

        // Από default όλοι οι νέοι χρήστες είναι φοιτητές
        person.setType(PersonType.STUDENT);

        return personRepository.save(person).getId();
    }
}
