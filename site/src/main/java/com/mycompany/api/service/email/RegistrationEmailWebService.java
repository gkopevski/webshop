package com.mycompany.api.service.email;

import java.io.IOException;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface RegistrationEmailWebService {
	void sendRegistrationEmail(Customer customer) throws IOException;
}
