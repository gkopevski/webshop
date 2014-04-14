/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.core.web.checkout.validator;

import org.broadleafcommerce.core.web.checkout.model.BillingInfoForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blBillingInfoFormValidator")
public class BillingInfoFormValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(BillingInfoForm.class);
    }

    public void validate(Object obj, Errors errors) {
        BillingInfoForm billingInfoForm = (BillingInfoForm) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.lastName", "lastName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.addressLine1", "addressLine1.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.city", "city.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.postalCode", "postalCode.required");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.phonePrimary", "phone.required");

//        if (billingInfoForm.getAddress().getCountry() == null) {
//            errors.rejectValue("address.country", "country.required", null, null);
//        }

        String validPersonNameRegex = "^[a-zA-ZéëïóöüÉËÏÓÖÜ0-9?][a-zA-ZéëïóöüÉËÏÓÖÜ0-9-?'.,/@!+:()]*";
        String validCompanyNameRegex = "^[a-zA-ZéëïóöüÉËÏÓÖÜ0-9?][a-zA-ZéëïóöüÉËÏÓÖÜ0-9-.',/+()#@!?]*";
        String validPhoneNumberRegex = "[a-zA-ZéëïóöüÉËÏÓÖÜ0-9-()+,*.#/]*";
        if (billingInfoForm.getAddress().getCountry() == null) {
            errors.rejectValue("address.country", "country.required", null, null);
        }

        if (!billingInfoForm.getAddress().getFirstName().equals("") && !billingInfoForm.getAddress().getFirstName().matches(validPersonNameRegex)) {
            errors.rejectValue("address.firstName", null, null, "firstName.invalid");
        }

        if (!billingInfoForm.getAddress().getLastName().equals("") && !billingInfoForm.getAddress().getLastName().matches(validPersonNameRegex)) {
            errors.rejectValue("address.lastName", null, null, "lastName.invalid");
        }

        String text = null;

        if (billingInfoForm.getAddress().getPhonePrimary() == null || billingInfoForm.getAddress().getPhonePrimary().getPhoneNumber().equals(""))
            text = "Phone is required.";
        else

        {
	        if (!billingInfoForm.getAddress().getPhonePrimary().getPhoneNumber().matches(validPhoneNumberRegex))
	            text = "Phone is invalid.";
        }
        
        billingInfoForm.getAddress().getPrimaryPhone();
        
        if( text != null)
        	errors.rejectValue("address.phonePrimary", null, null, text);

//         if (billingInfoForm.getAddress().getPhonePrimary() == null)
//             errors.rejectValue("address.phonePrimary", null, null, "it is null");
 //
//         if (billingInfoForm.getAddress().getPhonePrimary() != null && billingInfoForm.getAddress().getPhonePrimary().getPhoneNumber().trim().equals("vk")) {
//             errors.rejectValue("address.phonePrimary", null, null, "vojdan");
//         }

//         if (billingInfoForm.getAddress().getPhonePrimary() != null && !billingInfoForm.getAddress().getPhonePrimary().getPhoneNumber().trim().equals("") && !billingInfoForm.getAddress().getPhonePrimary().getPhoneNumber().matches(validPhoneNumberRegex)) {
//             errors.rejectValue("address.phonePrimary", null, null, "phone.invalid");
//         }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardName", "creditCardName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardNumber", "creditCardNumber.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardCvvCode", "creditCardCvvCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpMonth", "creditCardExpMonth.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpYear", "creditCardExpYear.required");
    }
}
