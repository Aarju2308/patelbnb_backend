package com.patelbnb.booking.mapper;

import com.patelbnb.booking.application.dto.BookedDateDTO;
import com.patelbnb.booking.application.dto.NewBookingDTO;
import com.patelbnb.booking.domain.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking newBookingToBooking(NewBookingDTO newBookingDTO);

    BookedDateDTO bookingToCheckAvailability(Booking booking);
}
