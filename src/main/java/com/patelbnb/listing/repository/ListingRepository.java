package com.patelbnb.listing.repository;

import com.patelbnb.listing.domain.BookingCategory;
import com.patelbnb.listing.domain.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing,Long> {

    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
        " WHERE listing.landlordPublicId = :publicId AND picture.isCover = true")
    List<Listing> findAllByLandlordPublicIdFetchCoverPicture(@Param("publicId") UUID publicId);

    long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

    Listing findByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE listing.bookingCategory = :bookingCategory AND picture.isCover = true")
    Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);


    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE picture.isCover = true")
    Page<Listing> findAllWithCoverOnly(Pageable pageable);

    Optional<Listing> findByPublicId(UUID publicId);

    @Query("SELECT l FROM Listing l LEFT JOIN FETCH l.pictures p WHERE l.publicId IN :publicIds AND p.isCover = true")
    List<Listing> findAllByPublicIdIn(List<UUID> publicIds);

    Optional<Listing> findOneByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId);

    Page<Listing> findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(
            Pageable pageable, String location, int bathrooms, int bedrooms, int guests, int beds
    );
}
