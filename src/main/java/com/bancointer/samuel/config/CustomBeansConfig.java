package com.bancointer.samuel.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomBeansConfig {

	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}
