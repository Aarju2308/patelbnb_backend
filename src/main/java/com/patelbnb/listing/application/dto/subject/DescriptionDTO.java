package com.patelbnb.listing.application.dto.subject;

import com.patelbnb.listing.application.dto.valueobject.DescriptionVO;
import com.patelbnb.listing.application.dto.valueobject.TitleVO;
import jakarta.validation.constraints.NotNull;

public record DescriptionDTO(
        @NotNull TitleVO title,
        @NotNull DescriptionVO description
        ) {
}
