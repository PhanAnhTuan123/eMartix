package com.eMartix.cart_service;

import com.eMartix.cart_service.security.SecurityConfig;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@OpenAPIDefinition(
		info = @Info(
				title = "Cart Service REST APIs",
				description = "Cart Service REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Phan Anh Tuan",
						email = "phantuan7a5@gmail.com",
						url = ""
				),
				license = @License(
						name = "Apache 2.0",
						url = ""
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Cart-Service Doc",
				url = ""
		)
)
@SpringBootApplication
@Import(SecurityConfig.class)
public class CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}
