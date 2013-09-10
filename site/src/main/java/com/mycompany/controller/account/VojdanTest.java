package com.mycompany.controller.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VojdanTest {
	
	@RequestMapping(value="/vojdantest",method=RequestMethod.GET)
	public String vojdantest(HttpServletRequest request, HttpServletResponse response){
		return "account/vojdantest";
	}

}
