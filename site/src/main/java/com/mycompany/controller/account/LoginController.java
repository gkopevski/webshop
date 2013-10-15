/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycompany.controller.account;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.security.util.PasswordUtils;
import org.broadleafcommerce.common.service.GenericResponse;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.core.web.controller.account.BroadleafLoginController;
import org.broadleafcommerce.core.web.controller.account.ResetPasswordForm;
import org.broadleafcommerce.profile.core.dao.CustomerForgotPasswordSecurityTokenDaoImpl;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerForgotPasswordSecurityToken;
import org.broadleafcommerce.profile.core.domain.CustomerForgotPasswordSecurityTokenImpl;
import org.hibernate.Session;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.api.service.email.RegistrationService;

/**
 * The controller responsible for all actions involving logging a customer in
 */
@Controller
public class LoginController extends BroadleafLoginController {
    
	@Resource(name="blCustomerForgotPasswordSecurityTokenDao")
    protected CustomerForgotPasswordSecurityTokenDaoImpl customerForgotPasswordSecurityTokenDao;
	
	@Resource(name="blPasswordEncoder")
    protected PasswordEncoder passwordEncoder;
	
	@Resource(name="blUserDetailsService")
	private UserDetailsService userDetailsService;
	
	
	@PersistenceContext(unitName = "blPU")
    protected EntityManager em;
	
	private SaltSource salt;
	
	@Resource(name = "registrationService")
	RegistrationService registrationEmailService;
	
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.login(request, response, model);
    }
    
    @RequestMapping(value="/login/forgotPassword", method=RequestMethod.GET)
    public String forgotPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.forgotPassword(request, response, model);
    }
    
    @RequestMapping(value="/login/forgotPassword", method=RequestMethod.POST)
    public String processForgotPassword(@RequestParam("emailAddress") String emailAddress, HttpServletRequest request, Model model) {
//    	 GenericResponse errorResponse = sendForgotPasswordNotification(emailAddress, getResetPasswordUrl(request));
    	 GenericResponse errorResponse = customerService.sendForgotPasswordNotification(emailAddress, getResetPasswordUrl(request));
         if (errorResponse.getHasErrors()) {
              String errorCode = errorResponse.getErrorCodesList().get(0);
              model.addAttribute("errorCode", errorCode);             
              return getForgotPasswordView();
         } else {
             request.getSession(true).setAttribute("forgot_password_username", emailAddress);
             return getForgotPasswordSuccessView();
         }
    }   

    @RequestMapping(value="/login/resetPassword", method=RequestMethod.GET)
    public String resetPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.resetPassword(request, response, model);
    }   
    
    @RequestMapping(value="/login/resetPassword", method=RequestMethod.POST)
    public String processResetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, HttpServletRequest request, HttpServletResponse response, Model model, BindingResult errors) throws ServiceException {
        return super.processResetPassword(resetPasswordForm, request, response, model, errors);
    }   
    
    @Override
    public String getResetPasswordUrl(HttpServletRequest request) {     
        String url = request.getScheme() + "://" + request.getServerName() + getResetPasswordPort(request, request.getScheme() + "/");
        
        if (request.getContextPath() != null && ! "".equals(request.getContextPath())) {
            url = url + request.getContextPath() + "/login/resetPassword";
        } else {
            url = url + "/login/resetPassword";
        }
        return url;
    }
    
    public GenericResponse sendForgotPasswordNotification(String username, String resetPasswordUrl) {
        GenericResponse response = new GenericResponse();
        Customer customer = null;

        if (username != null) {
            customer = customerService.readCustomerByUsername(username);
        }

        checkCustomer(customer,response);

        if (! response.getHasErrors()) {        
            String token = PasswordUtils.generateTemporaryPassword(20);
            token = token.toLowerCase();

            CustomerForgotPasswordSecurityToken fpst = new CustomerForgotPasswordSecurityTokenImpl();
            fpst.setCustomerId(customer.getId());            
            
            
            UserDetails principal = userDetailsService.loadUserByUsername(username);
	       	 salt = new SaltSource() {
	   			@Override
	   			public Object getSalt(UserDetails user) {
	   				return "mir!";
	   			}
	   		};
            

            fpst.setToken(passwordEncoder.encodePassword(token, salt.getSalt(principal)));
            fpst.setCreateDate(SystemTime.asDate());
            
//            em.merge(fpst);
            //GK: custom database insert/update
            Session hSession=em.unwrap(Session.class);
            hSession.saveOrUpdate(fpst);
            
//            fpst = customerForgotPasswordSecurityTokenDao.saveToken(fpst);

            HashMap<String, Object> vars = new HashMap<String, Object>();
            vars.put("token", token);
            if (!StringUtils.isEmpty(resetPasswordUrl)) {
                if (resetPasswordUrl.contains("?")) {
                    resetPasswordUrl=resetPasswordUrl+"&token="+fpst.getToken();
                } else {
                    resetPasswordUrl=resetPasswordUrl+"?token="+fpst.getToken();
                }
            }
            vars.put("resetPasswordUrl", resetPasswordUrl); 
            
            try {
				registrationEmailService.sendPasswordEmail(customer.getEmailAddress(), vars);
			} catch (Exception e) {
				//Problem with sending email
				e.printStackTrace();
			} 
        }
        return response;
    }
    
    protected void checkCustomer(Customer customer, GenericResponse response) {       
        if (customer == null) {         
            response.addErrorCode("invalidCustomer");
        } else if (customer.getEmailAddress() == null || "".equals(customer.getEmailAddress())) {
            response.addErrorCode("emailNotFound");
        } else if (customer.isDeactivated()) {
            response.addErrorCode("inactiveUser");
        }
    }
    
}
