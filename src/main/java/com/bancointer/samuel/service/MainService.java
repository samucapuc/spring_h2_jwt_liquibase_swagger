package com.bancointer.samuel.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {
	private MessageSource message;
	
	public String getMessageEnglish(String key, Object[]... args) {
		return message.getMessage(key, args, Locale.US);
	}
}
