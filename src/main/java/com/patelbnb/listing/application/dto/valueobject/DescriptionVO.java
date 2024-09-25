package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record DescriptionVO(@NotNull(message = "Description value cannot be null") String value) {
}
