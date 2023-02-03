package de.jkueck.blackstar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class BlackstarApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackstarApiApplication.class, args);
    }

}
