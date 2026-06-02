package com.homistay.dto.response;

import com.homistay.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class BookingResponse {
    private Long id;
    private Long guestId;
    private String guestName;
    private Long propertyId;
    private String propertyTitle;
    private String propertyCity;
    private String propertyImage;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer guestsCount;
    private Integer adults;
    private Integer children;
    private Integer infants;
    private Integer pets;
    private Integer nights;
    private BigDecimal totalPrice;
    private BigDecimal refundAmount;
    private BookingStatus status;
    private PaymentResponse payment;
    private LocalDateTime createdAt;
    private String specialRequests;
    private String specialRequestStatus;
    private String hostNotes;
    private String guestEmail;
    private String guestPhone;
    private String checkInInstructions;
    private List<BookingAddonResponse> addons;
}
