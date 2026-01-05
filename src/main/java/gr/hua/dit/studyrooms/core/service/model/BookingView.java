package gr.hua.dit.studyrooms.core.service.model;

import java.time.LocalDateTime;

// Χρησιμοποιούμε Java Records που είναι πιο σύντομα για DTOs
public record BookingView(
    Long id,
    String studentName,    // Ποιος το έκλεισε (π.χ. "Giannis Foititis")
    String roomName,       // Ποια αίθουσα (π.χ. "Αίθουσα Α")
    LocalDateTime startTime,
    LocalDateTime endTime,
    String status          // PENDING, CONFIRMED, etc.
) {
}
