package hr.algebra.onlineshop.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartsDTO {
    List<CartItemsDTO> items;
    BigDecimal totalPrice;
}
