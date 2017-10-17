package com.amor.arrow;

import com.amor.arrow.listener.ApplicationStartOverListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArrowApplicationLaucher {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ArrowApplicationLaucher.class);
		springApplication.addListeners(new ApplicationStartOverListener());
		springApplication.run(args);
	}
}
