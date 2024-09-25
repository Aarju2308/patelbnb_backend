package com.patelbnb.listing.mapper;

import com.patelbnb.listing.application.dto.*;
import com.patelbnb.listing.application.dto.valueobject.PriceVO;
import com.patelbnb.listing.domain.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ListingPictureMapper.class})
public interface ListingMapper {

    @Mapping(target = "landlordPublicId", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "pictures", ignore = true)
    @Mapping(target = "title", source = "description.title.value")
    @Mapping(target = "description", source = "description.description.value")
    @Mapping(target = "bedrooms", source = "infos.bedrooms.value")
    @Mapping(target = "guests", source = "infos.guests.value")
    @Mapping(target = "bookingCategory", source = "bookingCategory")
    @Mapping(target = "beds", source = "infos.beds.value")
    @Mapping(target = "bathrooms", source = "infos.baths.value")
    @Mapping(target = "price", source = "price.value")
    Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "publicId", target = "publicId")
    @Mapping(source = "bookingCategory", target = "bookingCategory")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "guests", target = "infos.guests.value")
    @Mapping(source = "bedrooms", target = "infos.bedrooms.value")
    @Mapping(source = "beds", target = "infos.beds.value")
    @Mapping(source = "bathrooms", target = "infos.baths.value")
    @Mapping(source = "description", target = "description.description.value")
    @Mapping(source = "title", target = "description.title.value")
    @Mapping(source = "price", target = "price.value")
    @Mapping(source = "pictures", target = "pictures")
    @Mapping(source = "landlordPublicId", target = "landlordPublicId")
    EditListingDTO toEditListingDTO(Listing listing);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "publicId", target = "publicId")
    @Mapping(source = "bookingCategory", target = "bookingCategory")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "infos.guests.value", target = "guests")
    @Mapping(source = "infos.bedrooms.value", target = "bedrooms")
    @Mapping(source = "infos.beds.value", target = "beds")
    @Mapping(source = "infos.baths.value", target = "bathrooms")
    @Mapping(source = "description.description.value", target = "description")
    @Mapping(source = "description.title.value", target = "title")
    @Mapping(source = "price.value", target = "price")
    @Mapping(source = "pictures", target = "pictures")
    @Mapping(source = "landlordPublicId", target = "landlordPublicId")
    Listing saveEditListingDTOToListing(EditListingDTO editListingDTO);

    CreatedListingDTO listingToCreatedListingDTO(Listing listing);

    @Mapping(target = "cover", source = "pictures")
    List<DisplayCardListingDTO> listingToDisplayCardListingDTOs(List<Listing> listings);

    @Mapping(target = "cover", source = "pictures", qualifiedByName = "extract-cover")
    DisplayCardListingDTO listingToDisplayCardListingDTO(Listing listing);

    default PriceVO mapPriceVoToPrice(int price){
        return new PriceVO(price);
    }

    @Mapping(target = "landlord", ignore = true)
    @Mapping(target = "description.title.value", source = "title")
    @Mapping(target = "description.description.value", source = "description")
    @Mapping(target = "infos.bedrooms.value", source = "bedrooms")
    @Mapping(target = "infos.guests.value", source = "guests")
    @Mapping(target = "infos.beds.value", source = "beds")
    @Mapping(target = "infos.baths.value", source = "bathrooms")
    @Mapping(target = "category", source = "bookingCategory")
    @Mapping(target = "price.value", source = "price")
    DisplayListingDTO listingToDisplayListingDTO(Listing listing);

    @Mapping(target = "listingPublicId", source = "publicId")
    ListingCreateBookingDTO mapListingToListingCreateBookingDTO(Listing listing);
}
