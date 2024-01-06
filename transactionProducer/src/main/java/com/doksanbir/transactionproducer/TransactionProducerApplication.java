package com.doksanbir.transactionproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TransactionProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionProducerApplication.class, args);
	}

}
