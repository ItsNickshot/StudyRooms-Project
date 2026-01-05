package gr.hua.dit.studyrooms.core.service.impl;

import gr.hua.dit.studyrooms.core.repository.PersonRepository;
import gr.hua.dit.studyrooms.core.service.PersonDataService;
import gr.hua.dit.studyrooms.core.service.mapper.PersonMapper;
import gr.hua.dit.studyrooms.core.service.model.PersonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonDataServiceImpl implements PersonDataService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonDataServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<PersonView> findAll() {
        return personRepository.findAll().stream()
            .map(personMapper::toView) // Διόρθωση ονόματος μεθόδου
            .collect(Collectors.toList());
    }
}
