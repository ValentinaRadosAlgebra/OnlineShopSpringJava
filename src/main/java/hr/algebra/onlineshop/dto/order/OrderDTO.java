package hr.algebra.onlineshop.dto.order;

import hr.algebra.onlineshop.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private LocalDateTime purchaseDate;
    private PaymentMethod paymentMethod;
    private BigDecimal totalPrice;
    private List<OrderItemDTO> items;
    private String username;
}
