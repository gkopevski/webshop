package com.mycompany.api.service.email;

import java.io.IOException;
import java.util.Date;

public interface MyEmailWebService {
	void sendOrderConfirmation(Date orderDate, String orderId, String emailAddress) throws IOException;
}
