package com.patelbnb.listing.application.dto.subject;

import jakarta.validation.constraints.NotNull;

public record LandlordDTO(
        @NotNull String firstName,
        @NotNull String imageUrl

) {
}
