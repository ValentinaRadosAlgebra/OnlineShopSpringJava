package hr.algebra.onlineshop.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final APIContext apiContext;

    public Payment createPayment(Double total, String currency, String method, String intent,
                                 String description, String cancelUrl, String successUrl)
            throws PayPalRESTException {

        //create total amount
        Amount amount = new Amount(currency,
                String.format(Locale.forLanguageTag(currency), "%.2f", total));

        //create transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        //create payer
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        //create payment
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(new ArrayList<>(
                List.of(transaction)
        ));

        //create error and success urls
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    //execute payment
    public Payment executePayment(String payerId, String paymentId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

}
