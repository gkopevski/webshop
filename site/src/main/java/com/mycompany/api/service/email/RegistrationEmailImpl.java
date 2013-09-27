package com.mycompany.api.service.email;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service("registrationEmailService")
public class RegistrationEmailImpl implements ApplicationContextAware, RegistrationEmailWebService{

	@Resource(name="blEmailService")
	protected EmailService emailService;
	
//	@Resource(name="blMessageCreator")
//	protected MessageCreator creator;
	
	protected ApplicationContext applicationContext;
	
//	@Resource(name="blRegistrationEmailInfo")
//	protected EmailInfo registrationEmailInfo;
	
	
	public void sendRegistrationEmail(Customer customer) throws IOException {
//		Customer customer = customerDao.readCustomerByEmail(emailAddress);
		HashMap<String, Object> props = new HashMap<String, Object>();
//	    props.put("customer.firstName", customer.getFirstName());
		props.put("customer", customer);
//		props.put("firstName", customer.getFirstName());
		
		//same error ??? the last 2 lines
		
//		creator.buildMessageBody(getRegistrationEmailInfo(), props);
//		EmailTargetImpl emailTargetImpl = new EmailTargetImpl();
		
//		emailTargetImpl.setEmailAddress(customer.getEmailAddress());
		
//		emailService.sendBasicEmail(getRegistrationEmailInfo(), emailTargetImpl, props);
	    emailService.sendTemplateEmail(customer.getEmailAddress(), getRegistrationEmailInfo(), props);
	}
	
	// Method based injection because we need to reference prototype scoped beans in a singleton bean
	protected EmailInfo getRegistrationEmailInfo() {
		return (EmailInfo) applicationContext.getBean("blRegistrationEmailInfo");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
}
