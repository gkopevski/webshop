package com.mycompany.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.core.checkout.service.exception.CheckoutException;
import org.broadleafcommerce.core.payment.service.exception.PaymentException;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.vendor.paypal.web.controller.BroadleafPayPalController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PayPalController extends BroadleafPayPalController {

    //This is the URL that will initiate the checkout process with PayPal.
    @RequestMapping("/paypal/checkout")
    public String paypalCheckout(HttpServletRequest request) throws PaymentException {
        return super.paypalCheckout(request);
    }

    //This is the URL that PayPal will redirect back to on callback.
    //This should match ${paypal.return.url} in your properties file.
    //For example:  ${paypal.return.url}=http://localhost:8080/mycompany/paypal/process
    @RequestMapping("/paypal/process")
    public String paypalProcess(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam String token, @RequestParam("PayerID") String payerID) throws CheckoutException, PricingException {
        return super.paypalProcess(request, response, model, token, payerID);
    }

}