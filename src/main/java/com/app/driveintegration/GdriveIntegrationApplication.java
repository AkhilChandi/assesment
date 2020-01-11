package com.app.driveintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class GdriveIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdriveIntegrationApplication.class, args);

    }
    @RequestMapping(value = "/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
