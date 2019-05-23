package com.bancointer.samuel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
@Configuration
@Profile(value = { "dev","test" })
public class PopulationDataBase implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		// TODO use para inicializar algo (script banco) quando o servidor subir
		
	}

}
