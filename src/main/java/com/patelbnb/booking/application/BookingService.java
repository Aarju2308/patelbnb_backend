package com.patelbnb.booking.application;

import com.patelbnb.booking.application.dto.BookedDateDTO;
import com.patelbnb.booking.application.dto.BookedListingDTO;
import com.patelbnb.booking.application.dto.NewBookingDTO;
import com.patelbnb.booking.domain.Booking;
import com.patelbnb.booking.mapper.BookingMapper;
import com.patelbnb.booking.repository.BookingRepository;
import com.patelbnb.infrastructure.config.SecurityUtils;
import com.patelbnb.listing.application.LandlordService;
import com.patelbnb.listing.application.dto.DisplayCardListingDTO;
import com.patelbnb.listing.application.dto.ListingCreateBookingDTO;
import com.patelbnb.listing.application.dto.valueobject.PriceVO;
import com.patelbnb.sharedkernel.service.State;
import com.patelbnb.user.application.UserService;
import com.patelbnb.user.application.dto.ReadUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final LandlordService landlordService;

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper, UserService userService, LandlordService landlordService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.landlordService = landlordService;
    }

    public State<Void, String> create(NewBookingDTO newBookingDTO){

        Booking booking = bookingMapper.newBookingToBooking(newBookingDTO);

        Optional<ListingCreateBookingDTO> listingOpt = landlordService.getByListingPublicId(newBookingDTO.listingPublicId());

        if (listingOpt.isEmpty()) {
            return State.<Void, String>builder().forError("Landlord public id not found");
        }

        boolean alreadyBooked = bookingRepository.bookingExistAtInterval(newBookingDTO.startDate(), newBookingDTO.endDate(), newBookingDTO.listingPublicId());

        if (alreadyBooked){
            return State.<Void, String>builder().forError("One booking already exists");
        }

        ListingCreateBookingDTO createBookingDTO = listingOpt.get();

        booking.setFkListing(createBookingDTO.listingPublicId());

        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        booking.setFkTenant(connectedUser.publicId());
        booking.setNumberOfTravelers(1);

        long numberOfNights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        booking.setTotalPrice((int) (numberOfNights * createBookingDTO.price().value()));

        bookingRepository.save(booking);

        return State.<Void, String>builder().forSuccess();
    }

    @Transactional(readOnly = true)
    public List<BookedDateDTO> checkAvailability(UUID publicId){
        return bookingRepository.findAllByFkListing(publicId)
                .stream().map(bookingMapper::bookingToCheckAvailability).toList();
    }

    @Transactional(readOnly = true)
    public List<BookedListingDTO> getBookedListings(){
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        List<Booking> allBooking = bookingRepository.findAllByFkTenant(connectedUser.publicId());
        List<UUID> allListingPublicIds = allBooking.stream().map(Booking::getFkListing).toList();
        List<DisplayCardListingDTO> allListings = landlordService.getCardDisplayByPublicIds(allListingPublicIds);
        return mapBookingToBookedListing(allBooking, allListings);
    }

    private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBooking, List<DisplayCardListingDTO> allListings) {
        return allBooking.stream().map(booking -> {
            DisplayCardListingDTO dto = allListings
                    .stream()
                    .filter(listing -> listing.publicId().equals(booking.getFkListing()))
                    .findFirst()
                    .orElseThrow();
            BookedDateDTO dates = bookingMapper.bookingToCheckAvailability(booking);
            return new BookedListingDTO(dto.cover(),
                    dto.location(),
                    dates,
                    new PriceVO(booking.getTotalPrice()),
                    booking.getPublicId(),
                    dto.publicId());
        }).toList();
    }

    @Transactional
    public State<UUID, String> cancelBooking(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord) {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
         int deleteSuccess = 0;

         if (SecurityUtils.hasCurrentUserAnyOfAuthorities(SecurityUtils.ROLE_LANDLORD)
         && byLandlord){
             deleteSuccess = handleDeletionForLandlord(bookingPublicId, listingPublicId, connectedUser, deleteSuccess);
         }else{
             deleteSuccess = bookingRepository.deleteBookingByFkTenantAndPublicId(connectedUser.publicId(), bookingPublicId);
         }

         if (deleteSuccess >= 1){
             return State.<UUID, String>builder().forSuccess(bookingPublicId);
         }else{
             return State.<UUID, String>builder().forError("Booking not found");
         }
    }

    private int handleDeletionForLandlord(UUID bookingPublicId, UUID listingPublicId, ReadUserDTO connectedUser, int deleteSuccess) {
        Optional<DisplayCardListingDTO> listingVerificationOptional = landlordService.getByPublicIdAndLandlordPublicId(listingPublicId, connectedUser.publicId());
        if (listingVerificationOptional.isPresent()) {
            deleteSuccess = bookingRepository.deleteBookingByPublicIdAndFkListing(bookingPublicId, listingVerificationOptional.get().publicId());
        }
        return deleteSuccess;
    }

    @Transactional(readOnly = true)
    public List<BookedListingDTO> bookedListingForLandlord(){
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(connectedUser);
        List<UUID> allPropertyPublicIds = allProperties.stream().map(DisplayCardListingDTO::publicId).toList();
        List<Booking> allBookings = bookingRepository.findAllByFkListingIn(allPropertyPublicIds);
        return mapBookingToBookedListing(allBookings, allProperties);

    }

    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, BookedDateDTO bookedDateDTO) {
        return bookingRepository.findAllMatchWithDate(listingsId, bookedDateDTO.startDate(), bookedDateDTO.endDate())
                .stream().map(Booking::getFkListing).toList();
    }

}
