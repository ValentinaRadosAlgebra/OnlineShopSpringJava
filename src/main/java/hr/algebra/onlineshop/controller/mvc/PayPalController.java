package hr.algebra.onlineshop.controller.mvc;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import hr.algebra.onlineshop.service.CartService;
import hr.algebra.onlineshop.service.PayPalService;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("mvc/paypal")
@RequiredArgsConstructor
@Slf4j
public class PayPalController {
    private final PayPalService paypalService;
    private final CartService cartService;
    private static final String PAYMENT_ERROR = "paymentError";
    @Value("${paypal.success-url}")
    private String successUrl;

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    @GetMapping("/create")
    public RedirectView createPayment(
            @RequestParam String method
    ) {
        try {
            double parsedAmount = cartService.getCart().getTotalPrice().doubleValue();

            Payment payment = paypalService.createPayment(
                    parsedAmount,
                    "EUR",
                    method,
                    "sale",
                    "Online Shop Purchase",
                    cancelUrl,
                    successUrl
            );
            return getApprovalUrl(payment);
        } catch (PayPalRESTException e) {
            log.error("Create payment exception::", e);
            return new RedirectView("/error");
        }
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(payerId, paymentId);
            return payment.getState().equals("approved") ? "paymentSuccess" : PAYMENT_ERROR;
        } catch (PayPalRESTException e) {
            log.error("Payment execution error::", e);
            return PAYMENT_ERROR;
        }
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return PAYMENT_ERROR;
    }

    private RedirectView getApprovalUrl(Payment payment) {
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url"))
                return new RedirectView(link.getHref());
        }

        return new RedirectView("/error");
    }
}
