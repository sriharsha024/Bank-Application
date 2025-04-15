package com.bank.BankApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Application",
				description = "This is a REST API for the backend application of bank services.",
				version = "1.0.0",
				contact = @Contact(
						name = "Harsha",
						email = "harsha@gmail.com"
				),
				license = @License(
						name = "The Bank App License"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Bank Application Full Documentation",
				url = "http://localhost:8080/swagger-ui/index.html"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
