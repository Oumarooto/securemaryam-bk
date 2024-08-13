package io.jokers.e_maryam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EMaryamApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMaryamApplication.class, args);
	}

}
