package com.mycompany.api.service.email;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;


@Service("registrationService")
public class RegistrationService implements ApplicationContextAware{

	 @Resource(name="mailSender")
	 private JavaMailSender mailSender;
	 
	 @Resource(name="velocityEngine")
	 private VelocityEngine velocityEngine;
	 
	 protected ApplicationContext applicationContext;

	   public void setMailSender(JavaMailSender mailSender) {
	      this.mailSender = mailSender;
	   }

	   public void setVelocityEngine(VelocityEngine velocityEngine) {
	      this.velocityEngine = velocityEngine;
	   }

	   public void register(Customer customer) throws Exception{
	      // Do the registration logic...
	      sendConfirmationEmail(customer);
	   }

	   private void sendConfirmationEmail(final Customer customer) throws Exception {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(customer.getEmailAddress());
	            message.setFrom("gkopevski@gmail.com"); // could be parameterized...
	            Map model = new HashMap();
	            model.put("customer", customer);
	            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/mycompany/api/service/email/registration-conformation.vm", model);
	            message.setText(text, true);
	         }
	      };
	      this.mailSender.send(preparator);
	   }

	   @Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
			
		}
	   
	   public void sendPasswordEmail(final String customerEmail, final Map vars) throws Exception {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(customerEmail);
	            message.setFrom("gkopevski@gmail.com"); // could be parameterized...
	            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/mycompany/api/service/email/resetPassword-email.vm", vars);
	            message.setText(text, true);
	         }
	      };
	      this.mailSender.send(preparator);
	   }
	   
}
