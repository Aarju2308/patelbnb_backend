package com.patelbnb.listing.application.dto;

import com.patelbnb.booking.application.dto.BookedDateDTO;
import com.patelbnb.listing.application.dto.subject.ListingInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SearchDTO(@Valid BookedDateDTO dates,
                        @Valid ListingInfoDTO infos,
                        @NotEmpty String location) {
}
