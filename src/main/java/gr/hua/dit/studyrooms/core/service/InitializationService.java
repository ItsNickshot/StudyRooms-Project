package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.model.Person;
import gr.hua.dit.studyrooms.core.model.PersonType;
import gr.hua.dit.studyrooms.core.model.StudyRoom;
import gr.hua.dit.studyrooms.core.repository.PersonRepository;
import gr.hua.dit.studyrooms.core.repository.StudyRoomRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InitializationService {

    private final PersonRepository personRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final PasswordEncoder passwordEncoder;

    public InitializationService(PersonRepository personRepository,
                                 StudyRoomRepository studyRoomRepository,
                                 PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // 1. Δημιουργία Χρηστών αν δεν υπάρχουν
        if (personRepository.count() == 0) {

            // ADMIN
            Person admin = new Person();
            admin.setUsername("admin");
            admin.setEmail("admin@hua.gr");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setType(PersonType.ADMIN);
            admin.setFirstName("Admin");
            admin.setLastName("User");
            personRepository.save(admin);

            // STUDENT
            Person student = new Person();
            student.setUsername("student");
            student.setEmail("it21000@hua.gr");
            student.setPassword(passwordEncoder.encode("1234"));
            student.setType(PersonType.STUDENT);
            student.setFirstName("Giannis");
            student.setLastName("Foititis");
            personRepository.save(student);

            // STAFF (Υπάλληλος Βιβλιοθήκης)
            Person staff = new Person();
            staff.setUsername("staff");
            staff.setEmail("staff@hua.gr");
            staff.setPassword(passwordEncoder.encode("1234"));
            staff.setType(PersonType.STAFF);
            staff.setFirstName("Maria");
            staff.setLastName("Bibliothikarios");
            personRepository.save(staff);

            System.out.println(">>> Users created: admin/1234, student/1234, staff/1234");
        }

        // 2. Δημιουργία Αιθουσών αν δεν υπάρχουν
        if (studyRoomRepository.count() == 0) {
            StudyRoom room1 = new StudyRoom("Αίθουσα Α (Ησυχίας)", 10, true);
            studyRoomRepository.save(room1);

            StudyRoom room2 = new StudyRoom("Αίθουσα Β (Ομαδική)", 6, true);
            studyRoomRepository.save(room2);

            StudyRoom room3 = new StudyRoom("PC Lab", 20, false); // Κλειστό για συντήρηση
            studyRoomRepository.save(room3);

            System.out.println(">>> Study Rooms created!");
        }
    }
}
