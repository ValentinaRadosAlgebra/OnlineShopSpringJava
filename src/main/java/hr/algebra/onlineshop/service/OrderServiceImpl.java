package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.cart.CartItemsDTO;
import hr.algebra.onlineshop.dto.cart.CartsDTO;
import hr.algebra.onlineshop.dto.order.CheckoutDTO;
import hr.algebra.onlineshop.dto.order.OrderDTO;
import hr.algebra.onlineshop.dto.order.OrderItemDTO;
import hr.algebra.onlineshop.model.*;
import hr.algebra.onlineshop.repo.*;
import hr.algebra.onlineshop.specification.OrderSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void checkout(CheckoutDTO dto) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartsDTO cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setPurchaseDate(LocalDateTime.now(ZoneId.of("UTC")));
        order.setTotalPrice(cart.getTotalPrice());

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName(dto.getPaymentMethod())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        order.setPaymentMethod(paymentMethod);

        order = orderRepository.save(order);

        for (CartItemsDTO item : cart.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderedItems oi = new OrderedItems();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(item.getQuantity());
            oi.setPrice(product.getPrice());

            order.getItems().add(oi);
        }

        orderRepository.save(order);

        cartService.clearCart();
    }

    @Override
    public List<OrderDTO> getUserOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        String username = authentication.getName();

        return orderRepository.findByUserUsername(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public OrderDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return toDTO(order);
    }

    @Override
    public List<OrderDTO> searchOrders(String username,
                                       LocalDateTime beginTime,
                                       LocalDateTime endTime) {

        Specification<Order> spec = Specification.allOf();

        if(username != null && !username.isBlank()){
            spec = spec.and(OrderSpecification.containsUsername(username));
        }

        if(beginTime != null){
            spec = spec.and(OrderSpecification.purchaseAfter(beginTime));
        }

        if(endTime != null){
            spec = spec.and(OrderSpecification.purchaseBefore(endTime));
        }

        return orderRepository.findAll(spec)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private OrderDTO toDTO(Order order) {

        List<OrderItemDTO> items = order.getItems()
                .stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getPurchaseDate(),
                order.getPaymentMethod(),
                order.getTotalPrice(),
                items,
                order.getUser().getUsername()
        );
    }
}
