package gr.hua.dit.studyrooms.web.rest;

import gr.hua.dit.studyrooms.core.service.PersonDataService;
import gr.hua.dit.studyrooms.core.service.model.PersonView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PersonResource {

    private final PersonDataService personDataService;

    public PersonResource(PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<PersonView>> getPeople() {
        // ΕΔΩ ΗΤΑΝ ΤΟ ΛΑΘΟΣ: Αλλαγή από getAllPeople() σε findAll()
        return ResponseEntity.ok(personDataService.findAll());
    }
}
