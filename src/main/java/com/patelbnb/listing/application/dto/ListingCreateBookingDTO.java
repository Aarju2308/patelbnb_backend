package com.patelbnb.listing.application.dto;

import com.patelbnb.listing.application.dto.valueobject.PriceVO;

import java.util.UUID;

public record ListingCreateBookingDTO(
        UUID listingPublicId,
        PriceVO price
) {
}
