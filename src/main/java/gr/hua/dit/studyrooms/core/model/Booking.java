package gr.hua.dit.studyrooms.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Person student; // Ποιος έκανε την κράτηση (Φοιτητής)

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private StudyRoom room; // Ποια αίθουσα έκλεισε

    @NotNull
    private LocalDateTime startTime; // Πότε αρχίζει

    @NotNull
    private LocalDateTime endTime; // Πότε τελειώνει

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // Constructors
    public Booking() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Person getStudent() { return student; }
    public void setStudent(Person student) { this.student = student; }

    public StudyRoom getRoom() { return room; }
    public void setRoom(StudyRoom room) { this.room = room; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
