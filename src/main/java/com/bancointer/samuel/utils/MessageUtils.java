package com.bancointer.samuel.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class MessageUtils {
	@Autowired
	private MessageSource message;
	
	public String getMessageEnglish(String key) {
		return message.getMessage(key, null, Locale.US);
	}
	public String getMessageEnglish(String key, Object[] args) {
		return message.getMessage(key, args, Locale.US);
	}
}
