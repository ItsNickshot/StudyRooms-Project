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

import java.time.LocalDateTime;
import java.time.LocalTime; // Χρειάζεται για το ωράριο
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    // Ορίζουμε το ωράριο λειτουργίας (π.χ. 08:00 - 20:00)
    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);

    private final BookingRepository bookingRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CurrentUserProvider currentUserProvider;
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
        LocalDateTime now = LocalDateTime.now();

        // 1. Έλεγχος αν η ημερομηνία είναι στο παρελθόν
        if (request.startTime().isBefore(now)) {
            throw new RuntimeException("Σφάλμα: Η ημερομηνία/ώρα έναρξης έχει ήδη περάσει.");
        }

        // 2. Έλεγχος αν η ώρα λήξης είναι πριν την ώρα έναρξης
        if (request.endTime().isBefore(request.startTime())) {
            throw new RuntimeException("Σφάλμα: Η ώρα λήξης δεν μπορεί να είναι πριν την ώρα έναρξης.");
        }

        // --- ΝΕΟΣ ΕΛΕΓΧΟΣ: Ωράριο Λειτουργίας ---
        LocalTime startTime = request.startTime().toLocalTime();
        LocalTime endTime = request.endTime().toLocalTime();

        if (startTime.isBefore(OPENING_TIME) || endTime.isAfter(CLOSING_TIME)) {
            throw new RuntimeException(
                String.format("Η βιβλιοθήκη λειτουργεί από τις %s έως τις %s.", OPENING_TIME, CLOSING_TIME)
            );
        }
        // ----------------------------------------

        // 3. Βρες ποιος χρήστης κάνει το αίτημα
        Person student = currentUserProvider.requireCurrentUser().getPerson();

        // 4. Έλεγχος: Είναι όντως φοιτητής;
        if (student.getType() != PersonType.STUDENT) {
            throw new RuntimeException("Μόνο φοιτητές μπορούν να κάνουν κρατήσεις.");
        }

        // 5. Έλεγχος: Είναι αργία; (Εξωτερική Υπηρεσία)
        if (holidayPort.isHoliday(request.startTime().toLocalDate())) {
            throw new RuntimeException("Η βιβλιοθήκη είναι κλειστή λόγω επίσημης αργίας.");
        }

        // 6. Έλεγχος: Υπάρχει το δωμάτιο;
        StudyRoom room = studyRoomRepository.findById(request.roomId())
            .orElseThrow(() -> new RuntimeException("Το δωμάτιο δεν βρέθηκε."));

        if (!room.getActive()) {
            throw new RuntimeException("Το δωμάτιο είναι εκτός λειτουργίας.");
        }

        // 7. Έλεγχος Επικάλυψης (Overlapping)
        boolean isOccupied = bookingRepository.existsOverlappingBooking(
            request.roomId(), request.startTime(), request.endTime()
        );

        if (isOccupied) {
            throw new RuntimeException("Η αίθουσα είναι ήδη κρατημένη για τις επιλεγμένες ώρες.");
        }

        // 8. Δημιουργία και Αποθήκευση
        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setRoom(room);
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toView(savedBooking);
    }

    /**
     * Ακύρωση Κράτησης (Admin/Staff ή Owner).
     */
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Η κράτηση δεν βρέθηκε"));

        Person currentUser = currentUserProvider.requireCurrentUser().getPerson();

        boolean isStaffOrAdmin = currentUser.getType() == PersonType.ADMIN || currentUser.getType() == PersonType.STAFF;
        boolean isOwner = booking.getStudent().getId().equals(currentUser.getId());

        if (!isStaffOrAdmin && !isOwner) {
            throw new RuntimeException("Δεν έχετε δικαίωμα να ακυρώσετε αυτή την κράτηση.");
        }

        if (booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        }
    }

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

    public List<StudyRoom> getAllActiveRooms() {
        return studyRoomRepository.findByIsActiveTrue();
    }

    public void createRoom(String name, Integer capacity) {
        StudyRoom room = new StudyRoom();
        room.setName(name);
        room.setCapacity(capacity);
        room.setActive(true); // Την κάνουμε ενεργή από προεπιλογή
        studyRoomRepository.save(room);
    }

    public List<StudyRoom> getAllRooms() {
        return studyRoomRepository.findAll();
    }

    public void deleteRoom(Long roomId) {
        // (Προαιρετικά) Σβήσε πρώτα τις κρατήσεις αν θες να μην σκάει:
        // bookingRepository.deleteAll(bookingRepository.findByRoomId(roomId)); (θέλει υλοποίηση στο repo)

        studyRoomRepository.deleteById(roomId);
    }

    public StudyRoom getRoomById(Long id) {
        return studyRoomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Η αίθουσα δεν βρέθηκε"));
    }

    public void updateRoom(Long id, String name, Integer capacity, Boolean isActive) {
        StudyRoom room = getRoomById(id);
        room.setName(name);
        room.setCapacity(capacity);
        room.setActive(isActive); // Εδώ αλλάζουμε το status
        studyRoomRepository.save(room);
    }
}
