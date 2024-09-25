package com.patelbnb.listing.mapper;

import com.patelbnb.listing.application.dto.subject.PictureDTO;
import com.patelbnb.listing.domain.ListingPicture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ListingPictureMapper {

    @Mapping(target = "listingId", ignore = true)
    @Mapping(target = "listing", ignore = true)
    Set<ListingPicture> pictureDTOToListingPicture(List<PictureDTO> pictures);

    List<PictureDTO> listingPictureToPictureDTO(List<ListingPicture> listingPictures);

    @Mapping(target = "isCover", source = "cover")
    PictureDTO convertToPictureDTO(ListingPicture listingPicture);

    @Named("extract-cover")
    default PictureDTO extractCover(Set<ListingPicture> listingPictures){
        return listingPictures.stream().findFirst().map(this::convertToPictureDTO).orElseThrow();
    }

}