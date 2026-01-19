package gr.hua.dit.studyrooms.core.repository;

import gr.hua.dit.studyrooms.core.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Εύρεση κρατήσεων ενός φοιτητή
    List<Booking> findByStudentId(Long studentId);

    // Εύρεση κρατήσεων για ένα συγκεκριμένο δωμάτιο (π.χ. για να δούμε το πρόγραμμά του)
    List<Booking> findByRoomId(Long roomId);

    /*
     * QUERY ΓΙΑ ΕΛΕΓΧΟ ΕΠΙΚΑΛΥΨΗΣ (OVERLAPPING)
     * Ελέγχει αν υπάρχει ήδη κράτηση (b) στο ίδιο δωμάτιο (:roomId)
     * η οποία είναι ΕΝΕΡΓΗ (CONFIRMED ή PENDING)
     * και οι χρόνοι συμπίπτουν.
     * Λογική επικάλυψης: (StartA < EndB) και (EndA > StartB)
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
        "WHERE b.room.id = :roomId " +
        "AND (b.status = 'CONFIRMED' OR b.status = 'PENDING') " +
        "AND (b.startTime < :endTime AND b.endTime > :startTime)")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    // Για τον κανόνα: "Ο φοιτητής μπορεί να έχει μέχρι Χ ενεργές κρατήσεις"
    // Μετράμε πόσες κρατήσεις έχει που είναι CONFIRMED ή PENDING
    @Query("SELECT COUNT(b) FROM Booking b " +
        "WHERE b.student.id = :studentId " +
        "AND (b.status = 'CONFIRMED' OR b.status = 'PENDING')")
    long countActiveBookings(@Param("studentId") Long studentId);
}
