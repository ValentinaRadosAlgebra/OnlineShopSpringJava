package hr.algebra.onlineshop.service;

import hr.algebra.onlineshop.dto.cart.CartsDTO;

public interface CartService {
    void addProduct(Long productId, Integer quantity);
    void updateProduct(Long productId, Integer quantity);
    void removeProduct(Long productId);
    void clearCart();
    CartsDTO getCart();
    void mergeSessionCartToDatabase(String username);
}
