package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.model.*;
import gr.hua.dit.studyrooms.core.port.HolidayPort;
import gr.hua.dit.studyrooms.core.repository.BookingRepository;
import gr.hua.dit.studyrooms.core.repository.StudyRoomRepository;
import gr.hua.dit.studyrooms.core.security.CurrentUserProvider;
import gr.hua.dit.studyrooms.core.service.mapper.BookingMapper;
import gr.hua.dit.studyrooms.core.service.model.BookingView;
import gr.hua.dit.studyrooms.core.service.model.CreateBookingRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CurrentUserProvider currentUserProvider; // Μας δίνει τον logged-in χρήστη
    private final HolidayPort holidayPort;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository,
                          StudyRoomRepository studyRoomRepository,
                          CurrentUserProvider currentUserProvider,
                          HolidayPort holidayPort,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.currentUserProvider = currentUserProvider;
        this.holidayPort = holidayPort;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Δημιουργία νέας κράτησης με όλους τους ελέγχους.
     */
    public BookingView createBooking(CreateBookingRequest request) {
        // 1. Βρες ποιος χρήστης κάνει το αίτημα
        Person student = currentUserProvider.requireCurrentUser().getPerson();

        // 2. Έλεγχος: Είναι όντως φοιτητής;
        if (student.getType() != PersonType.STUDENT) {
            throw new RuntimeException("Μόνο φοιτητές μπορούν να κάνουν κρατήσεις.");
        }

        // 3. Έλεγχος: Είναι αργία; (Εξωτερική Υπηρεσία)
        if (holidayPort.isHoliday(request.startTime().toLocalDate())) {
            throw new RuntimeException("Η βιβλιοθήκη είναι κλειστή λόγω επίσημης αργίας.");
        }

        // 4. Έλεγχος: Υπάρχει το δωμάτιο;
        StudyRoom room = studyRoomRepository.findById(request.roomId())
            .orElseThrow(() -> new RuntimeException("Το δωμάτιο δεν βρέθηκε."));

        if (!room.getActive()) {
            throw new RuntimeException("Το δωμάτιο είναι εκτός λειτουργίας.");
        }

        // 5. Έλεγχος Επικάλυψης (Overlapping)
        boolean isOccupied = bookingRepository.existsOverlappingBooking(
            request.roomId(), request.startTime(), request.endTime()
        );

        if (isOccupied) {
            throw new RuntimeException("Η αίθουσα είναι ήδη κρατημένη για τις επιλεγμένες ώρες.");
        }

        // 6. Δημιουργία και Αποθήκευση
        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setRoom(room);
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setStatus(BookingStatus.CONFIRMED); // Ή PENDING αν θες έγκριση

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toView(savedBooking);
    }

    /**
     * Επιστρέφει τις κρατήσεις του συνδεδεμένου χρήστη.
     */
    public List<BookingView> getMyBookings() {
        Person user = currentUserProvider.requireCurrentUser().getPerson();
        return bookingRepository.findByStudentId(user.getId())
            .stream()
            .map(bookingMapper::toView)
            .collect(Collectors.toList());
    }

    public List<BookingView> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(bookingMapper::toView)
            .collect(Collectors.toList());
    }

    /**
     * Επιστρέφει όλα τα διαθέσιμα δωμάτια.
     */
    public List<StudyRoom> getAllActiveRooms() {
        return studyRoomRepository.findByIsActiveTrue();
    }
}
