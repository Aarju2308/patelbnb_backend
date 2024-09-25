package com.patelbnb.listing.application.dto;

import com.patelbnb.listing.application.dto.subject.PictureDTO;
import com.patelbnb.listing.application.dto.valueobject.PriceVO;
import com.patelbnb.listing.domain.BookingCategory;

import java.util.UUID;

public record DisplayCardListingDTO(PriceVO price,
                                    String location,
                                    PictureDTO cover,
                                    BookingCategory bookingCategory,
                                    UUID publicId) {
}
