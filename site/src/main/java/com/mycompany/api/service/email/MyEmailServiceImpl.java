package com.mycompany.api.service.email;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;



import javax.annotation.Resource;

import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("myEmailService")
public class MyEmailServiceImpl implements ApplicationContextAware, MyEmailWebService{

	@Resource(name="blEmailService")
	protected EmailService emailService;

	protected ApplicationContext applicationContext;

	public void sendOrderConfirmation(Date orderDate, String orderId, String emailAddress) throws IOException {
	    HashMap<String, Object> props = new HashMap<String, Object>();
	    props.put("orderDate", orderDate);
	    props.put("orderId", orderId);
	    emailService.sendTemplateEmail(emailAddress, getOrderConfirmationEmailInfo(), props);
	}

	// Method based injection because we need to reference prototype scoped beans in a singleton bean
	protected EmailInfo getOrderConfirmationEmailInfo() {
	    return (EmailInfo) applicationContext.getBean("orderConfirmationEmailInfo");
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    this.applicationContext = applicationContext;
	}
}
