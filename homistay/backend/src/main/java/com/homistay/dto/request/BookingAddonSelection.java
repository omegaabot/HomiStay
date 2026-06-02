package com.homistay.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingAddonSelection {
    @NotNull
    private Long addonId;

    @Min(1)
    private int quantity = 1;
}
