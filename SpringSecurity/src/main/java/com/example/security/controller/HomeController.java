package com.example.security.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping(path = { "/", "/home" })
	public String Home() {
		return "home.jsp";
	}

	@RequestMapping("/login")
	public String LoginPage() {
		return "login.jsp";
	}

	@RequestMapping("/logout-success")
	public String LogoutPage() {
		return "logout.jsp";
	}

	@RequestMapping("user")
	@ResponseBody
	public Principal user(Principal principal) {
		return principal;
	}
}
