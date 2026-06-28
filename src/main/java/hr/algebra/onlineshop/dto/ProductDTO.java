package hr.algebra.onlineshop.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @NotEmpty(message = "Product name is required")
    private String name;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
