package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record BedroomsVO(@NotNull(message = "Bedrooms must not be null") int value) {
}
