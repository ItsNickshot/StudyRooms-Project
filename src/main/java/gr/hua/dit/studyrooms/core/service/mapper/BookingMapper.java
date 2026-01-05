package gr.hua.dit.studyrooms.core.service.mapper;

import gr.hua.dit.studyrooms.core.model.Booking;
import gr.hua.dit.studyrooms.core.service.model.BookingView;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingView toView(Booking booking) {
        return new BookingView(
            booking.getId(),
            booking.getStudent().getFirstName() + " " + booking.getStudent().getLastName(),
            booking.getRoom().getName(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getStatus().name()
        );
    }
}
