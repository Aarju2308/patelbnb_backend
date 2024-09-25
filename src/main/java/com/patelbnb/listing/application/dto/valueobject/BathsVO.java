package com.patelbnb.listing.application.dto.valueobject;

import jakarta.validation.constraints.NotNull;

public record BathsVO(@NotNull(message = "Bath Value Must Be Required") int value) {
}
