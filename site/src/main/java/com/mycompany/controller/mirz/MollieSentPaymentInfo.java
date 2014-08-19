package com.mycompany.controller.mirz;

import com.google.gson.Gson;
import mollie.PaymentInformationModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.web.controller.checkout.BroadleafCheckoutController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.profile.core.domain.PhoneImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
/**
 * Created by gkopevski on 5/20/14.
 */
@Controller
@RequestMapping("/mollie")
public class MollieSentPaymentInfo {



    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Transactional("blTransactionManager")
    @RequestMapping(value = "/checkout",
            method = {RequestMethod.GET})
    public String testGet() {
        try {
            return mirDoGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/process",
            method = {RequestMethod.GET})
    public String testGet(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }


    @RequestMapping(value = "/process",
            method = {RequestMethod.POST})
    public String testPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            printParameters(request);

            String paymentId = request.getParameter("id");
            HttpClient httpClient = new DefaultHttpClient();
            try{
                HttpGet requestV = new HttpGet("https://api.mollie.nl/v1/payments/"+paymentId);

//  StringEntity params = new StringEntity(
//    "{\"amount\":\"101.00\",\"redirectUrl\":\"http://88.85.115.54/LogWatch/FinishHandler\",\"description\":\"Order #40029\"}"
//  ); // I've removed details
                requestV.addHeader("Authorization", "pharmaskincare.nl test_JFN6oTJjnBZT0oRiNLvyIVS03C28Ny"); // This should be correct
                requestV.addHeader("Accept", "application/json"); // new header
                requestV.addHeader("Content-Type", "application/json"); // new header

                //requestV.setEntity(params);

                HttpResponse responseV = httpClient.execute(requestV);

                BufferedReader br = new BufferedReader(new InputStreamReader((responseV.getEntity().getContent())));

                String output;
                System.out.println("Output from Server:");
                while ((output = br.readLine()) != null) {
                    System.out.println(output + "\n");
                }

                Gson gResponse =  new Gson();
                PaymentInformationModel paymentInfo = gResponse.fromJson(br.readLine(), PaymentInformationModel.class);

            }
            catch (Exception e) {
                e.printStackTrace();
            }


                return "redirect:/cart";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Transactional("blTransactionManager")
    public String mirDoGet() {

        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost requestV = new HttpPost("https://api.mollie.nl/v1/payments");


            Order cart = CartState.getCart();
            em.persist(cart);

            StringEntity params = new StringEntity(
                    "{\"amount\":\"" +
                            cart.getTotal() +
//                            "101.00" +
                            "\",\"redirectUrl\":\"" +
                            "http://88.85.115.54/cart?id=" + cart.getId() +
                            "\",\"description\":\"" +
                            "Order #:" + cart.getOrderNumber() +
                            ", Pharmaskincare.nl" +
                            //"Order #40029" +
                            "\"}"
            ); // I've removed details
            requestV.addHeader("Authorization", "pharmaskincare.nl test_JFN6oTJjnBZT0oRiNLvyIVS03C28Ny"); // This should be correct
            requestV.addHeader("Accept", "application/json"); // new header
            requestV.addHeader("Content-Type", "application/json"); // new header

            requestV.setEntity(params);

            HttpResponse responseV = httpClient.execute(requestV);

            BufferedReader br = new BufferedReader(new InputStreamReader((responseV.getEntity().getContent())));

            String output;
            System.out.println("Output from Server:");

            Gson gResponse =  new Gson();
            PaymentInformationModel paymentInfo = gResponse.fromJson(br.readLine(), PaymentInformationModel.class);

//			while ((output = ) != null) {
//				System.out.println(output + "\n");
//			}

            if (responseV.getStatusLine().getStatusCode() < 200 || responseV.getStatusLine().getStatusCode() > 299) {
                throw new RuntimeException("Failed : HTTP error code : " + responseV.getStatusLine().getStatusCode());
            }

            //response.sendRedirect(paymentInfo.getLinks().getPaymentUrl());

            return "redirect:"+ paymentInfo.getLinks().getPaymentUrl();
            //return;

        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return "";//if exception is caught, maybe redirect to home page?

    }

    protected void printParameters (HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> enumq = request.getParameterNames();
        System.out.println("Translator.printParameters list: " + request.getMethod() + " " + request.getRequestURI() + " " + new Date());
        if (!enumq.hasMoreElements() )
            System.out.println("- no parameters found.");
        while (enumq.hasMoreElements() ) {
            String parameter = (String) enumq.nextElement();
            String[] values = request.getParameterValues(parameter);
            for (int i = 0; i < values.length; i++ ) {
                System.out.println("- " + parameter + ": " + values[i]);
            }
        }
    }
}
