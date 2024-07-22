package proj.concert.service.mapper;


import proj.concert.service.domain.Seat;
import proj.concert.service.domain.Booking;

import java.util.ArrayList;
import java.util.List;



/**
 * Mapper class to convert from domain-model to DTO objects representing
 * Bookings.
 */


public class BookingMapper {
    // method to convert booking to bookingdto
    public static proj.concert.common.dto.BookingDTO toDto(Booking booking) {
        List<proj.concert.common.dto.SeatDTO> seatDtos = new ArrayList<>();
        for (Seat seat: booking.getSeats()) {
            proj.concert.common.dto.SeatDTO seatDto = SeatMapper.toDto(seat);
            seatDtos.add(seatDto);
        }
        proj.concert.common.dto.BookingDTO dtoBooking =
                new proj.concert.common.dto.BookingDTO(
                        booking.getConcertId(),
                        booking.getDate(),
                        seatDtos);
        return dtoBooking;

    }

}
