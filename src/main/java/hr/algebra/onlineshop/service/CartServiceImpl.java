package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.cart.CartItemsDTO;
import hr.algebra.onlineshop.dto.cart.CartsDTO;
import hr.algebra.onlineshop.model.CartItems;
import hr.algebra.onlineshop.model.Carts;
import hr.algebra.onlineshop.model.Product;
import hr.algebra.onlineshop.model.User;
import hr.algebra.onlineshop.repo.CartRepository;
import hr.algebra.onlineshop.repo.ProductRepository;
import hr.algebra.onlineshop.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final HttpSession session;

    private static final String SESSION_CART = "SESSION_CART";


    @Override
    public void addProduct(Long productId, Integer quantity) {

        String username = getUsername();

        if (username == null) {
            Map<Long, Integer> cart =
                    (Map<Long, Integer>) session.getAttribute(SESSION_CART);

            if (cart == null) cart = new HashMap<>();

            cart.put(productId,
                    cart.getOrDefault(productId, 0) + quantity);

            session.setAttribute(SESSION_CART, cart);

        } else {
            Carts cart = cartRepository.findByUserId(getUserId(username))
                    .orElseGet(() -> {
                        Carts c = new Carts();
                        c.setUser(getUser(username));
                        return cartRepository.save(c);
                    });

            Product product = productRepository.findById(productId)
                    .orElseThrow();

            Optional<CartItems> existing =
                    cart.getItems()
                            .stream()
                            .filter(i -> i.getProduct().getId().equals(productId))
                            .findFirst();

            if (existing.isPresent()) {
                existing.get().setQuantity(
                        existing.get().getQuantity() + quantity
                );
            } else {
                CartItems item = new CartItems();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(quantity);
                cart.getItems().add(item);
            }

            cartRepository.save(cart);
        }
    }


    @Override
    public void updateProduct(Long productId, Integer quantity) {

        String username = getUsername();

        if (username == null) {
            Map<Long, Integer> cart =
                    (Map<Long, Integer>) session.getAttribute(SESSION_CART);

            if (cart == null) return;

            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                cart.put(productId, quantity);
            }

            session.setAttribute(SESSION_CART, cart);

        } else {

            Carts cart = cartRepository.findByUserId(getUserId(username))
                    .orElseThrow();

            CartItems item = cart.getItems()
                    .stream()
                    .filter(i -> i.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow();

            if (quantity <= 0) {
                cart.getItems().remove(item);
            } else {
                item.setQuantity(quantity);
            }

            cartRepository.save(cart);
        }
    }


    @Override
    public void removeProduct(Long productId) {

        String username = getUsername();

        if (username == null) {

            Map<Long, Integer> cart =
                    (Map<Long, Integer>) session.getAttribute(SESSION_CART);

            if (cart != null) {
                cart.remove(productId);
                session.setAttribute(SESSION_CART, cart);
            }

        } else {

            Carts cart = cartRepository.findByUserId(getUserId(username))
                    .orElseThrow();

            cart.getItems().removeIf(i ->
                    i.getProduct().getId().equals(productId));

            cartRepository.save(cart);
        }
    }

    @Override
    public void clearCart() {

        String username = getUsername();

        if (username == null) {
            session.removeAttribute(SESSION_CART);
        } else {
            Carts cart = cartRepository.findByUserId(getUserId(username))
                    .orElseThrow();

            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    @Override
    public CartsDTO getCart() {

        String username = getUsername();

        if (username == null) {
            Map<Long, Integer> sessionCart =
                    (Map<Long, Integer>) session.getAttribute(SESSION_CART);

            if (sessionCart == null)
                return new CartsDTO(List.of(), BigDecimal.ZERO);

            return mapSessionCart(sessionCart);
        }

        Carts cart = cartRepository.findByUserId(getUserId(username))
                .orElse(new Carts());

        return mapDbCart(cart);
    }

    private CartsDTO mapDbCart(Carts cart) {

        List<CartItemsDTO> items = cart.getItems()
                .stream()
                .map(i -> new CartItemsDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getProduct().getPrice(),
                        i.getQuantity()
                ))
                .toList();

        BigDecimal total = cart.getItems()
                .stream()
                .map(i -> i.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartsDTO(items, total);
    }

    private CartsDTO mapSessionCart(Map<Long, Integer> sessionCart) {

        List<CartItemsDTO> items = new java.util.ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (var entry : sessionCart.entrySet()) {

            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow();

            BigDecimal price = product.getPrice()
                    .multiply(BigDecimal.valueOf(entry.getValue()));

            total = total.add(price);

            items.add(new CartItemsDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    entry.getValue()
            ));
        }

        return new CartsDTO(items, total);
    }

    @Transactional
    @Override
    public void mergeSessionCartToDatabase(String username) {

        Map<Long, Integer> sessionCart =
                (Map<Long, Integer>) session.getAttribute(SESSION_CART);

        if (sessionCart == null || sessionCart.isEmpty()) return;

        Carts cart = cartRepository.findByUserId(getUserId(username))
                .orElseGet(() -> {
                    Carts c = new Carts();
                    c.setUser(getUser(username));
                    return cartRepository.save(c);
                });

        for (var entry : sessionCart.entrySet()) {

            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Optional<CartItems> existingItem = cart.getItems()
                    .stream()
                    .filter(i -> i.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(
                        existingItem.get().getQuantity() + entry.getValue()
                );
            } else {
                CartItems item = new CartItems();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(entry.getValue());
                cart.getItems().add(item);
            }
        }

        cartRepository.save(cart);
        session.removeAttribute(SESSION_CART);
    }

    private Long getUserId(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String au = "anonymousUser";
        if (auth == null || au.equals(auth.getPrincipal())) {
            return null;
        }
        return auth.getName();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}