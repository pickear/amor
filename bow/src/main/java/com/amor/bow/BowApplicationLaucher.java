package com.amor.bow;

import com.amor.bow.listener.ApplicationStartOverListener;
import com.amor.bow.proxy.BowProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BowApplicationLaucher {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BowApplicationLaucher.class);
		springApplication.addListeners(new ApplicationStartOverListener());
		springApplication.run(args);
	}
}
