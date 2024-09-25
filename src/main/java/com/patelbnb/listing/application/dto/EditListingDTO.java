package com.patelbnb.listing.application.dto;

import com.patelbnb.listing.application.dto.subject.DescriptionDTO;
import com.patelbnb.listing.application.dto.subject.LandlordDTO;
import com.patelbnb.listing.application.dto.subject.ListingInfoDTO;
import com.patelbnb.listing.application.dto.subject.PictureDTO;
import com.patelbnb.listing.application.dto.valueobject.PriceVO;
import com.patelbnb.listing.domain.BookingCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class EditListingDTO {
    @NotNull
    private long id;

    @NotNull
    private UUID publicId;

    @NotNull
    private UUID landlordPublicId;

    @NotNull
    BookingCategory bookingCategory;

    @NotNull
    String location;

    @NotNull @Valid
    ListingInfoDTO infos;

    @NotNull @Valid
    DescriptionDTO description;

    @NotNull @Valid
    PriceVO price;

    @NotNull
    List<PictureDTO> pictures;

    private LandlordDTO landlord;

    @NotNull
    public long getId() {
        return id;
    }

    public void setId(@NotNull long id) {
        this.id = id;
    }

    public @NotNull UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(@NotNull UUID publicId) {
        this.publicId = publicId;
    }

    public @NotNull UUID getLandlordPublicId() {
        return landlordPublicId;
    }

    public void setLandlordPublicId(@NotNull UUID landlordPublicId) {
        this.landlordPublicId = landlordPublicId;
    }

    public @NotNull BookingCategory getBookingCategory() {
        return bookingCategory;
    }

    public void setBookingCategory(@NotNull BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
    }

    public @NotNull String getLocation() {
        return location;
    }

    public void setLocation(@NotNull String location) {
        this.location = location;
    }

    public @NotNull @Valid ListingInfoDTO getInfos() {
        return infos;
    }

    public void setInfos(@NotNull @Valid ListingInfoDTO infos) {
        this.infos = infos;
    }

    public @NotNull @Valid DescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(@NotNull @Valid DescriptionDTO description) {
        this.description = description;
    }

    public @NotNull @Valid PriceVO getPrice() {
        return price;
    }

    public void setPrice(@NotNull @Valid PriceVO price) {
        this.price = price;
    }

    public @NotNull List<PictureDTO> getPictures() {
        return pictures;
    }

    public void setPictures(@NotNull List<PictureDTO> pictures) {
        this.pictures = pictures;
    }


    @Override
    public String toString() {
        return "EditListingDTO{" +
                "id=" + id +
                ", publicId=" + publicId +
                ", landlordPublicId=" + landlordPublicId +
                ", bookingCategory=" + bookingCategory +
                ", location='" + location + '\'' +
                ", infos=" + infos +
                ", description=" + description +
                ", price=" + price +
                ", pictures=" + pictures +
                '}';
    }
}
