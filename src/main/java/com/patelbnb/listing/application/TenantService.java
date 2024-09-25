package com.patelbnb.listing.application;

import com.patelbnb.booking.application.BookingService;
import com.patelbnb.listing.application.dto.DisplayCardListingDTO;
import com.patelbnb.listing.application.dto.DisplayListingDTO;
import com.patelbnb.listing.application.dto.SearchDTO;
import com.patelbnb.listing.application.dto.subject.LandlordDTO;
import com.patelbnb.listing.domain.BookingCategory;
import com.patelbnb.listing.domain.Listing;
import com.patelbnb.listing.mapper.ListingMapper;
import com.patelbnb.listing.repository.ListingRepository;
import com.patelbnb.sharedkernel.service.State;
import com.patelbnb.user.application.UserService;
import com.patelbnb.user.application.dto.ReadUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService {

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserService userService;
    private final BookingService bookingService;

    public TenantService(ListingRepository listingRepository, ListingMapper listingMapper, UserService userService, BookingService bookingService) {
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    public Page<DisplayCardListingDTO> getAllByCategory(BookingCategory category, Pageable pageable){
        Page<Listing> allOrBookingCategory;
        if(category == BookingCategory.ALL){
            allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
        }else{
            allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(pageable, category);
        }

        return allOrBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);
    }

    @Transactional(readOnly = true)
    public State<DisplayListingDTO, String> getOne(UUID publicId){
        Optional<Listing> listingByPublicId = listingRepository.findByPublicId(publicId);

        if (listingByPublicId.isEmpty()){
            return State.<DisplayListingDTO, String>builder()
                    .forError(String.format("No listing found for public id %s", publicId));
        }

        DisplayListingDTO displayListingDTO = listingMapper.listingToDisplayListingDTO(listingByPublicId.get());
        ReadUserDTO readUserDTO = userService.getByPublicId(listingByPublicId.get().getLandlordPublicId()).orElseThrow();
        LandlordDTO landlordDTO = new LandlordDTO(readUserDTO.firstName(), readUserDTO.imageUrl());
        displayListingDTO.setLandlord(landlordDTO);

        return State.<DisplayListingDTO,String>builder().forSuccess(displayListingDTO);
    }

    @Transactional(readOnly = true)
    public Page<DisplayCardListingDTO> search(Pageable pageable, SearchDTO newSearch) {

        Page<Listing> allMatchedListings = listingRepository.findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(pageable, newSearch.location(),
                newSearch.infos().baths().value(),
                newSearch.infos().bedrooms().value(),
                newSearch.infos().guests().value(),
                newSearch.infos().beds().value());

        List<UUID> listingUUIDs = allMatchedListings.stream().map(Listing::getPublicId).toList();

        List<UUID> bookingUUIDs = bookingService.getBookingMatchByListingIdsAndBookedDate(listingUUIDs, newSearch.dates());

        List<DisplayCardListingDTO> listingsNotBooked = allMatchedListings.stream().filter(listing -> !bookingUUIDs.contains(listing.getPublicId()))
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();

        return new PageImpl<>(listingsNotBooked, pageable, listingsNotBooked.size());
    }
}
