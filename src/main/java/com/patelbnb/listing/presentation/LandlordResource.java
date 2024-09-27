package com.patelbnb.listing.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patelbnb.infrastructure.config.SecurityUtils;
import com.patelbnb.listing.application.LandlordService;
import com.patelbnb.listing.application.dto.CreatedListingDTO;
import com.patelbnb.listing.application.dto.DisplayCardListingDTO;
import com.patelbnb.listing.application.dto.EditListingDTO;
import com.patelbnb.listing.application.dto.SaveListingDTO;
import com.patelbnb.listing.application.dto.subject.PictureDTO;
import com.patelbnb.sharedkernel.service.State;
import com.patelbnb.sharedkernel.service.StatusNotification;
import com.patelbnb.user.application.UserException;
import com.patelbnb.user.application.UserService;
import com.patelbnb.user.application.dto.ReadUserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/landlord-listing")
public class LandlordResource {

    private final LandlordService landlordService;
    private final Validator validator;
    private final UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public LandlordResource(LandlordService landlordService, Validator validator, UserService userService) {
        this.landlordService = landlordService;
        this.validator = validator;
        this.userService = userService;
    }

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatedListingDTO> create(
            MultipartHttpServletRequest request,
            @RequestPart(name = "dto") String saveListingDTOString
    ) throws IOException {
        List<PictureDTO> pictures = request.getFileMap()
                .values()
                .stream()
                .map(mapMultiPartFileTOPictureDTO())
                .toList();

        SaveListingDTO saveListingDTO = objectMapper.readValue(saveListingDTOString, SaveListingDTO.class);
        saveListingDTO.setPictures(pictures);

        Set<ConstraintViolation<SaveListingDTO>> violations = validator.validate(saveListingDTO);

        if (!violations.isEmpty()){
            String violationJoined = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationJoined);
            return ResponseEntity.of(problemDetail).build();
        }else{
            return ResponseEntity.ok(landlordService.create(saveListingDTO));
        }

    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateListing(
            MultipartHttpServletRequest request,
            @RequestPart(name = "dto") String updateListingDTOString) throws IOException {
        List<PictureDTO> pictures = request.getFileMap()
                .values()
                .stream()
                .map(mapMultiPartFileTOPictureDTO())
                .toList();

        EditListingDTO editListingDTO = objectMapper.readValue(updateListingDTOString, EditListingDTO.class);
        editListingDTO.setPictures(pictures);

        Set<ConstraintViolation<EditListingDTO>> violations = validator.validate(editListingDTO);

        if (!violations.isEmpty()){
            String violationJoined = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationJoined);
            return ResponseEntity.of(problemDetail).build();
        }else{
            Long updateId = landlordService.update(editListingDTO);
            if(updateId != null){
                return ResponseEntity.ok(updateId);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }

    private static Function<MultipartFile, PictureDTO> mapMultiPartFileTOPictureDTO() {
        return multiPartFile -> {
            try {
                return new PictureDTO(multiPartFile.getBytes(), multiPartFile.getContentType(), false);
            } catch (IOException e) {
                throw new UserException(String.format("Could not parse multi-part file %s", multiPartFile.getOriginalFilename()));
            }
        };
    }

    @GetMapping(value = "/get-all")
    @PreAuthorize("hasAnyRole('"+ SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<List<DisplayCardListingDTO>> getAll(){
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(connectedUser);
        return ResponseEntity.ok(allProperties);
    }

    @GetMapping(value = "/get-single")
    @PreAuthorize("hasAnyRole('"+ SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<EditListingDTO> getSingle(@RequestParam("publicId") String publicId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(publicId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request if publicId is not a valid UUID
        }

        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        EditListingDTO singleListing = landlordService.getListing(uuid, connectedUser);
        if (!singleListing.getLandlordPublicId().equals(connectedUser.publicId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(singleListing);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('"+ SecurityUtils.ROLE_LANDLORD +"')")
    public ResponseEntity<UUID> delete(@RequestParam UUID publicId){
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSpringSecurity();
        State<UUID, String> deleteProperty = landlordService.delete(publicId, connectedUser);
        if (deleteProperty.getStatus().equals(StatusNotification.OK)) {
            return ResponseEntity.ok(deleteProperty.getValue());
        }else if (deleteProperty.getStatus().equals(StatusNotification.UNAUTHORIZED)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
