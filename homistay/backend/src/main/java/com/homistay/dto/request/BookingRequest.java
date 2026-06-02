package com.homistay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    @NotNull
    private Long propertyId;

    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;

    @NotNull @Min(1)
    private Integer guestsCount;

    private Integer adults;

    private Integer children;

    private Integer infants;

    private Integer pets;

    private String paymentMethod = "CARD";
    private String specialRequests;

    private List<BookingAddonSelection> addons;
}
