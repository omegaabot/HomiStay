package com.homistay.dto.request;

import com.homistay.enums.PropertyType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private PropertyType type;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;

    @NotNull @DecimalMin("1.00")
    private BigDecimal pricePerNight;

    @NotNull @Min(1) @Max(50)
    private Integer maxGuests;

    @Min(0) private Integer bedrooms = 1;
    @Min(0) private Integer bathrooms = 1;

    private String amenities;
    private List<String> imageUrls;
    private java.math.BigDecimal cleaningFee;
    private Boolean allowsChildren;
    private Boolean allowsInfants;
    private Boolean allowsPets;
    private String houseRules;
    private String guestRequirements;
    private String checkInInstructions;
}
