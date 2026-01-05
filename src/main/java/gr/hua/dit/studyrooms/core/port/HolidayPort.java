package gr.hua.dit.studyrooms.core.port;

import java.time.LocalDate;

public interface HolidayPort {
    /**
     * Ελέγχει αν μια ημερομηνία είναι επίσημη αργία.
     * @param date Η ημερομηνία προς έλεγχο
     * @return true αν είναι αργία, false αν είναι εργάσιμη
     */
    boolean isHoliday(LocalDate date);
}
