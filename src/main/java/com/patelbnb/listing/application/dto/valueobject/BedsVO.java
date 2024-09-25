package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record BedsVO(@NotNull(message = "Beds value must be present") int value) {
}
