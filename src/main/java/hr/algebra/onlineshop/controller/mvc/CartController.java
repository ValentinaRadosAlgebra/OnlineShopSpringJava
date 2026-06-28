package hr.algebra.onlineshop.controller.mvc;

import hr.algebra.onlineshop.dto.order.CheckoutDTO;
import hr.algebra.onlineshop.repo.PaymentMethodRepository;
import hr.algebra.onlineshop.service.CartService;
import hr.algebra.onlineshop.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mvc/cart")
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentMethodRepository paymentMethodRepository;

    public CartController (CartService cartService, OrderService orderService, PaymentMethodRepository paymentMethodRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.paymentMethodRepository = paymentMethodRepository;
    }
    private static final String REDIRECT_CART = "redirect:/mvc/cart";

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("paymentMethods", paymentMethodRepository.findAll());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam Integer quantity) {

        cartService.addProduct(productId, quantity);
        return "redirect:/mvc/home";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long productId,
                             @RequestParam Integer quantity) {

        cartService.updateProduct(productId, quantity);
        return REDIRECT_CART;
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long productId) {
        cartService.removeProduct(productId);
        return REDIRECT_CART;
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return REDIRECT_CART;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String paymentMethod) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName().equals("anonymousUser")) {
            return "redirect:/mvc/auth/login";
        }
        String paypal = "PayPal";
        if (paypal.equalsIgnoreCase(paymentMethod)) {
            return "redirect:/mvc/paypal/create?method=" + paymentMethod;
        }

            CheckoutDTO dto = new CheckoutDTO();
            dto.setPaymentMethod(paymentMethod);

            orderService.checkout(dto);

        return "redirect:/mvc/home";
    }
}
