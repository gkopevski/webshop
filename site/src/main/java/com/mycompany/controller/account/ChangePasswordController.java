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

package com.mycompany.controller.account;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.security.util.PasswordChange;
import org.broadleafcommerce.core.web.controller.account.BroadleafChangePasswordController;
import org.broadleafcommerce.core.web.controller.account.ChangePasswordForm;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account/password")
public class ChangePasswordController extends BroadleafChangePasswordController {
	
	@Resource(name="blPasswordEncoder")
	private Md5PasswordEncoder blPasswordEncoder = new Md5PasswordEncoder();
	
	private SaltSource salt;
	
	 @Resource(name="blUserDetailsService")
	 private UserDetailsService userDetailsService;
	 
	 @Resource(name="blCustomerService")
	 CustomerService customerService;
	 
    @RequestMapping(method = RequestMethod.GET)
    public String viewChangePassword(HttpServletRequest request, Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm form) {
        return super.viewChangePassword(request, model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processChangePassword(HttpServletRequest request, Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
//    	passwordChange.setCurrentPassword(passwordEncoder.encodePassword(form.getCurrentPassword(), getSalt(CustomerState.getCustomer()));

    	 PasswordChange passwordChange = new PasswordChange(CustomerState.getCustomer().getUsername());
    	 UserDetails principal = userDetailsService.loadUserByUsername(CustomerState.getCustomer().getUsername());
    	 
    	 salt = new SaltSource() {
			@Override
			public Object getSalt(UserDetails user) {
				return "mir!";
			}
		};

         passwordChange.setCurrentPassword(blPasswordEncoder.encodePassword(form.getCurrentPassword(),  salt.getSalt(principal)));
         
         passwordChange.setNewPassword(form.getNewPassword());
         passwordChange.setNewPasswordConfirm(form.getNewPasswordConfirm());
         changePasswordValidator.validate(passwordChange, result);
         if (result.hasErrors()) {
             return getChangePasswordView();
         }
         customerService.changePassword(passwordChange);
         return getChangePasswordRedirect();
         //return super.processChangePassword(request, model, form, result, redirectAttributes);
    }
    
}
