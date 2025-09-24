package com.jeisoneccel.my_finances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MyFinancesApplication {

	public static ConfigurableApplicationContext appContext;

	public static void main(String[] args) {
		appContext = SpringApplication.run(MyFinancesApplication.class, args);
	}

}
