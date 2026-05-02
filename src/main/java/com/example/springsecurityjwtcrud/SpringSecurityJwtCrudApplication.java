package com.example.springsecurityjwtcrud;

import com.example.springsecurityjwtcrud.security.AppJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppJwtProperties.class)
public class SpringSecurityJwtCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtCrudApplication.class, args);
    }
}
