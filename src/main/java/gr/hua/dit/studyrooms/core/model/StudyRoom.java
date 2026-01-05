package gr.hua.dit.studyrooms.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "study_room")
public class StudyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name; // π.χ. "Αίθουσα Μελέτης 1" ή "Θέση Α12"

    @NotNull
    private Integer capacity; // Πόσα άτομα χωράει

    @NotNull
    private Boolean isActive = true; // Αν είναι διαθέσιμη (true) ή κλειστή για συντήρηση (false)

    // Κενός Constructor (απαραίτητος για JPA)
    public StudyRoom() {
    }

    // Constructor με πεδία
    public StudyRoom(String name, Integer capacity, Boolean isActive) {
        this.name = name;
        this.capacity = capacity;
        this.isActive = isActive;
    }

    // Getters και Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "StudyRoom{id=" + id + ", name='" + name + "'}";
    }
}
