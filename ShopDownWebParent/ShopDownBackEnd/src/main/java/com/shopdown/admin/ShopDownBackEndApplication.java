package com.shopdown.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopdown.common.entity", "com.shopdown.admin.user"})
public class ShopDownBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopDownBackEndApplication.class, args);
	}

}
