/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycompany.controller.checkout;

import org.apache.velocity.app.VelocityEngine;
import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.web.controller.checkout.BroadleafOrderConfirmationController;
import org.broadleafcommerce.profile.core.dao.CustomerDao;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderConfirmationController extends BroadleafOrderConfirmationController {

    @Resource(name="blCustomerDao")
    protected CustomerDao customerDao;
    
    @Resource(name = "blEmailService")
    protected EmailService emailService;
    
    @Resource(name = "blOrderConfirmationEmailInfo")
    protected EmailInfo orderConfirmationEmailInfo;

    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    //VK
    @Resource(name="mailSender")
	 private JavaMailSender mailSender;
	 
	 @Resource(name="velocityEngine")
	 private VelocityEngine velocityEngine;

    @RequestMapping(value = "/confirmation/{orderNumber}", method = RequestMethod.GET)
    public String displayOrderConfirmationByOrderNumber(@PathVariable("orderNumber") String orderNumber, Model model,
            HttpServletRequest request, HttpServletResponse response) {
        
    	
    	
    	
    	try {
			sendConfirmationEmail(orderNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    	
    	
    	
    	return super.displayOrderConfirmationByOrderNumber(orderNumber, model, request, response);
    }
    
    public void sendConfirmationEmail(String orderNumber) throws Exception{
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        Customer customer = customerDao.readCustomerByEmail(order.getEmailAddress());
        
        
        
        if (customer != null){
            HashMap<String, Object> vars = new HashMap<String, Object>();
            vars.put("customer", customer);
            vars.put("orderNumber", orderNumber);
            vars.put("order", order);
            emailService.sendTemplateEmail(customer.getEmailAddress(), getOrderConfirmationEmailInfo(), vars);
            
            sendCustomerOrderConfirmationEmailMIR(customer);
            sendAdminOrderConfirmationEmailMIR(customer);
        }
        
        
    }
    
    private void sendCustomerOrderConfirmationEmailMIR(final Customer customer) throws Exception {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(customer.getEmailAddress());
	            message.setFrom("gkopevski@gmail.com"); // could be parameterized...
	            Map model = new HashMap();
	            model.put("customer", customer);
	            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/mycompany/api/service/email/order-confirmation.vm", model);
	            message.setText(text, true);
	         }
	      };
	      this.mailSender.send(preparator);
	}
    
    private void sendAdminOrderConfirmationEmailMIR(final Customer customer) throws Exception {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(customer.getEmailAddress());
	            message.setFrom("gkopevski@gmail.com"); // could be parameterized...
	            Map model = new HashMap();
	            model.put("customer", customer);
	            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/mycompany/api/service/email/order-confirmation.vm", model);
	            message.setText(text, true);
	         }
	      };
	      this.mailSender.send(preparator);
	}

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public EmailInfo getOrderConfirmationEmailInfo() {
        return orderConfirmationEmailInfo;
    }

    public void setOrderConfirmationEmailInfo(EmailInfo orderConfirmationEmailInfo) {
        this.orderConfirmationEmailInfo = orderConfirmationEmailInfo;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
