package hr.algebra.onlineshop.dto.order;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {
    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;
}
