package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateBookingRequest(
    @NotNull(message = "Πρέπει να επιλέξετε αίθουσα")
    Long roomId,

    @NotNull(message = "Η ώρα έναρξης είναι υποχρεωτική")
    @Future(message = "Η κράτηση πρέπει να είναι σε μελλοντικό χρόνο")
    LocalDateTime startTime,

    @NotNull(message = "Η ώρα λήξης είναι υποχρεωτική")
    LocalDateTime endTime
) {
}
