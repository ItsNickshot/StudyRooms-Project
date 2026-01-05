package gr.hua.dit.studyrooms.core.repository;

import gr.hua.dit.studyrooms.core.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
