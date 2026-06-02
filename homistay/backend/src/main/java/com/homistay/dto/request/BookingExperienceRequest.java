package com.homistay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class BookingExperienceRequest {
    @NotNull private Long experienceId;

    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull @Min(1)
    private Integer participants;
}
