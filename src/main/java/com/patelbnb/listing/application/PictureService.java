package com.patelbnb.listing.application;

import com.patelbnb.listing.application.dto.subject.PictureDTO;
import com.patelbnb.listing.domain.Listing;
import com.patelbnb.listing.domain.ListingPicture;
import com.patelbnb.listing.mapper.ListingPictureMapper;
import com.patelbnb.listing.repository.ListingPictureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PictureService {

    private final ListingPictureRepository listingPictureRepository;
    private final ListingPictureMapper listingPictureMapper;

    public PictureService(ListingPictureRepository listingPictureRepository, ListingPictureMapper listingPictureMapper) {
        this.listingPictureRepository = listingPictureRepository;
        this.listingPictureMapper = listingPictureMapper;
    }

    public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing){
        Set<ListingPicture> listingPictures = listingPictureMapper.pictureDTOToListingPicture(pictures);

        boolean isFirst = true;

        for(ListingPicture picture : listingPictures){
            picture.setCover(isFirst);
            picture.setListing(listing);
            isFirst = false;
        }

        listingPictureRepository.saveAll(listingPictures);
        return listingPictureMapper.listingPictureToPictureDTO(listingPictures.stream().toList());

    }

    public void removeAllPicturesFromListing(Listing listing) {
        List<ListingPicture> existingPictures = listingPictureRepository.findByListing(listing);
        listingPictureRepository.deleteAll(existingPictures);
    }
}
