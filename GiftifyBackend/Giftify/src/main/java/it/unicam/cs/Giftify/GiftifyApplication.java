package it.unicam.cs.Giftify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GiftifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftifyApplication.class, args);
	}
}
