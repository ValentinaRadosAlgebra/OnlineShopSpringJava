package hr.algebra.onlineshop.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity.longValue()));
    }
}
