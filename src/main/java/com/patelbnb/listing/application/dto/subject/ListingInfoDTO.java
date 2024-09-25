package com.patelbnb.listing.application.dto.subject;

import com.patelbnb.listing.application.dto.valueobject.BathsVO;
import com.patelbnb.listing.application.dto.valueobject.BedroomsVO;
import com.patelbnb.listing.application.dto.valueobject.BedsVO;
import com.patelbnb.listing.application.dto.valueobject.GuestsVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ListingInfoDTO(
        @NotNull @Valid GuestsVO guests,
        @NotNull @Valid BedroomsVO bedrooms,
        @NotNull @Valid BedsVO beds,
        @NotNull @Valid BathsVO baths) {
}
