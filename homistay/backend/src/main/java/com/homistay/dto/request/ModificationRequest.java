package com.homistay.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ModificationRequest {
    private LocalDate newCheckIn;
    private LocalDate newCheckOut;
    private Integer newGuests;
    private String reason;
}
