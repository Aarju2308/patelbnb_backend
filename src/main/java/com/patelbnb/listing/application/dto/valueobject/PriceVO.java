package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record PriceVO(@NotNull(message = "Price must be present") int value) {
}
