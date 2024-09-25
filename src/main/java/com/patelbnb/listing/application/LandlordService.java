package com.patelbnb.listing.application;

import com.patelbnb.listing.application.dto.*;
import com.patelbnb.listing.domain.Listing;
import com.patelbnb.listing.mapper.ListingMapper;
import com.patelbnb.listing.repository.ListingRepository;
import com.patelbnb.sharedkernel.service.State;
import com.patelbnb.user.application.Auth0Service;
import com.patelbnb.user.application.UserService;
import com.patelbnb.user.application.dto.ReadUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LandlordService {

    private final ListingRepository listingRepository;

    private final ListingMapper listingMapper;
    private final UserService userService;
    private final Auth0Service authService;
    private final PictureService pictureService;

    public LandlordService(ListingRepository listingRepository, ListingMapper listingMapper, UserService userService, Auth0Service authService, PictureService pictureService) {
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
        this.userService = userService;
        this.authService = authService;
        this.pictureService = pictureService;
    }

    public CreatedListingDTO create(SaveListingDTO saveListingDTO){
        Listing newListing = listingMapper.saveListingDTOToListing(saveListingDTO);

        ReadUserDTO userConnected = userService.getAuthenticatedUserFromSpringSecurity();

        newListing.setLandlordPublicId(userConnected.publicId());
        Listing savedListing = listingRepository.saveAndFlush(newListing);

        pictureService.saveAll(saveListingDTO.getPictures(), savedListing);
        authService.addLandlordRoleToUser(userConnected);
        return listingMapper.listingToCreatedListingDTO(savedListing);
    }

    @Transactional(readOnly = true)
    public List<DisplayCardListingDTO> getAllProperties(ReadUserDTO landlord){
        List<Listing> properties = listingRepository.findAllByLandlordPublicIdFetchCoverPicture(landlord.publicId());
        return listingMapper.listingToDisplayCardListingDTOs(properties);
    }

    @Transactional
    public State<UUID,String> delete(UUID publicId, ReadUserDTO landlord){
        long deletedId = listingRepository.deleteByPublicIdAndLandlordPublicId(publicId, landlord.publicId());
        if (deletedId > 0){
            return State.<UUID,String>builder().forSuccess(publicId);
        }else{
            return State.<UUID,String>builder().forUnauthorized("User not authorized to delete this listing");
        }
    }

    @Transactional(readOnly = true)
    public EditListingDTO getListing(UUID publicId, ReadUserDTO landlord){
        Listing byPublicIdAndLandlordPublicId = listingRepository.findByPublicIdAndLandlordPublicId(publicId, landlord.publicId());
        return listingMapper.toEditListingDTO(byPublicIdAndLandlordPublicId);
    }


    @Transactional
    public Long update(EditListingDTO editListingDTO) {
        Listing newListing = listingMapper.saveEditListingDTOToListing(editListingDTO);

        ReadUserDTO userConnected = userService.getAuthenticatedUserFromSpringSecurity();

        if (userConnected.publicId().equals(newListing.getLandlordPublicId())) {
            Listing existingListing = listingRepository.findById(newListing.getId())
                    .orElseThrow(() -> new RuntimeException("Listing not found"));

            // Remove existing pictures
            pictureService.removeAllPicturesFromListing(existingListing);

            Listing savedListing = listingRepository.saveAndFlush(newListing);        // Remove existing pictures
            pictureService.saveAll(editListingDTO.getPictures(), savedListing);
            return savedListing.getId();
        }

        return null;
    }

    public Optional<ListingCreateBookingDTO> getByListingPublicId(UUID publicId) {
        return listingRepository.findByPublicId(publicId).map(listingMapper::mapListingToListingCreateBookingDTO);
    }

    public List<DisplayCardListingDTO> getCardDisplayByPublicIds(List<UUID> allListingPublicIds) {
        return listingRepository.findAllByPublicIdIn(allListingPublicIds)
                .stream()
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId) {
        return listingRepository.findOneByPublicIdAndLandlordPublicId(listingPublicId, landlordPublicId)
                .map(listingMapper::listingToDisplayCardListingDTO);
    }
}
