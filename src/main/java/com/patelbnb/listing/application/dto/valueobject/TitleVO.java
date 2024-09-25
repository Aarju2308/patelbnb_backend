package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record TitleVO(@NotNull(message = "Title value muse be present") String value) {
}
