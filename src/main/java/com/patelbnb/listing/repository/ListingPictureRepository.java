package com.patelbnb.listing.repository;

import com.patelbnb.listing.domain.Listing;
import com.patelbnb.listing.domain.ListingPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingPictureRepository extends JpaRepository<ListingPicture,Long> {

    List<ListingPicture> findByListing(Listing listing);
}
