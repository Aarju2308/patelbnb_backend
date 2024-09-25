package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record GuestsVO(@NotNull(message = "Guests must not be null") int value) {
}
