package gr.hua.dit.studyrooms.web.ui.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateBookingForm {

    @NotNull(message = "Επιλέξτε αίθουσα")
    private Long roomId;

    @NotNull(message = "Επιλέξτε ώρα έναρξης")
    private LocalDateTime startTime;

    @NotNull(message = "Επιλέξτε ώρα λήξης")
    private LocalDateTime endTime;

    // Getters & Setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
