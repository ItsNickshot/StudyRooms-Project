package gr.hua.dit.studyrooms.core.port.impl;

import gr.hua.dit.studyrooms.core.port.HolidayPort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

@Service
public class HolidayPortImpl implements HolidayPort {

    private final RestTemplate restTemplate;

    public HolidayPortImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        // Καλούμε το δωρεάν API για τις αργίες της Ελλάδας (GR)
        String url = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/GR";

        try {
            ResponseEntity<PublicHolidayDTO[]> response = restTemplate.getForEntity(url, PublicHolidayDTO[].class);

            if (response.getBody() != null) {
                for (PublicHolidayDTO holiday : response.getBody()) {
                    // Το API επιστρέφει ημερομηνίες σε string format "YYYY-MM-DD"
                    if (holiday.date().equals(date.toString())) {
                        System.out.println(">>> Η κράτηση απορρίφθηκε λόγω αργίας: " + holiday.localName());
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την κλήση του Holiday API: " + e.getMessage());
            // Σε περίπτωση σφάλματος του external service, συνήθως επιτρέπουμε την κράτηση
            // ή πετάμε exception ανάλογα με την πολιτική μας. Εδώ το αγνοούμε.
        }

        return false;
    }

    // Ένα απλό record για να διαβάσουμε την απάντηση JSON
    private record PublicHolidayDTO(String date, String localName) {}
}
