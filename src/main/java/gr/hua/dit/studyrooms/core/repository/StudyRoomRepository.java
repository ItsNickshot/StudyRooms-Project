package gr.hua.dit.studyrooms.core.repository;

import gr.hua.dit.studyrooms.core.model.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {

    // Εύρεση μόνο των ενεργών δωματίων (που δεν είναι κλειστά για συντήρηση)
    List<StudyRoom> findByIsActiveTrue();
}
