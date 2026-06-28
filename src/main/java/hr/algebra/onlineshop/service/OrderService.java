package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.order.CheckoutDTO;
import hr.algebra.onlineshop.dto.order.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    void checkout(CheckoutDTO checkoutDTO);
    List<OrderDTO> getUserOrders();
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long id);
    List<OrderDTO> searchOrders(String username, LocalDateTime beginTime, LocalDateTime endTime);
}
