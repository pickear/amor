package com.amor.arrow;

import com.amor.arrow.listener.ApplicationStartOverListener;
import com.amor.arrow.proxy.ArrowProxy;
import com.amor.common.helper.KeepAliveHelper;
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
