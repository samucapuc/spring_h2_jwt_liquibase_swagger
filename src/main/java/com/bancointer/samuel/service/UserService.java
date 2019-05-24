package com.bancointer.samuel.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.bancointer.samuel.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
