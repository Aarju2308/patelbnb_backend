package com.patelbnb.booking.presentation;

import com.patelbnb.booking.application.BookingService;
import com.patelbnb.booking.application.dto.BookedDateDTO;
import com.patelbnb.booking.application.dto.BookedListingDTO;
import com.patelbnb.booking.application.dto.NewBookingDTO;
import com.patelbnb.infrastructure.config.SecurityUtils;
import com.patelbnb.sharedkernel.service.State;
import com.patelbnb.sharedkernel.service.StatusNotification;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingResource {

    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> create(@Valid @RequestBody NewBookingDTO newBookingDTO){
        State<Void, String> createState = bookingService.create(newBookingDTO);

        if (createState.getStatus().equals(StatusNotification.ERROR)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, createState.getError());

            return ResponseEntity.of(problemDetail).build();
        }else{
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/check-availability")
    public ResponseEntity<List<BookedDateDTO>> checkAvailability(@RequestParam UUID listingPublicId){
        return ResponseEntity.ok(bookingService.checkAvailability(listingPublicId));
    }

    @GetMapping("/get-booked-listing")
    public ResponseEntity<List<BookedListingDTO>> getBookedListings(){
        return ResponseEntity.ok(bookingService.getBookedListings());
    }

    @DeleteMapping("/cancel-booking")
    public ResponseEntity<UUID> cancel(@RequestParam UUID bookingPublicId,
                                       @RequestParam UUID listingPublicId,
                                       @RequestParam boolean byLandlord){
        State<UUID, String> cancelState = bookingService.cancelBooking(bookingPublicId, listingPublicId, byLandlord);
        if (cancelState.getStatus().equals(StatusNotification.ERROR)){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, cancelState.getError());
            return ResponseEntity.of(problemDetail).build();
        }else {
            return ResponseEntity.ok(bookingPublicId);
        }
    }

    @GetMapping("get-booked-listing-for-landlord")
    @PreAuthorize("hasAnyRole('"+ SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<List<BookedListingDTO>> getBookedListingForLandlord(){
        return ResponseEntity.ok(bookingService.bookedListingForLandlord());
    }

}