package com.homistay.dto.request;

import com.homistay.enums.PropertyType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PropertySearchRequest {
    private String city;
    private PropertyType type;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer guests;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;

    private int page = 0;
    private int size = 12;
    private String sortBy = "pricePerNight";
    private String sortDir = "asc";
}
