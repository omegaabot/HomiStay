package com.homistay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddonRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull @DecimalMin("0.01")
    private BigDecimal price;
}
