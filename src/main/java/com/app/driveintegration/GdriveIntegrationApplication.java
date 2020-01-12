package com.app.driveintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@SpringBootApplication
@RestController
@EnableWebMvc
@ApiIgnore
public class GdriveIntegrationApplication {

    @Autowired
    ServletContext servletContext;
    public static void main(String[] args) {
        SpringApplication.run(GdriveIntegrationApplication.class, args);

    }
    @RequestMapping(value = "/userinfo")
    public void login(Principal principal, HttpServletRequest httpServletRequest, HttpServletResponse httpServletRespons) throws IOException {
        String baseUrl = String.format("%s://%s:%d%s/swagger-ui.html",httpServletRequest.getScheme(),  httpServletRequest.getServerName(), httpServletRequest.getServerPort(),servletContext.getContextPath());
        httpServletRespons.sendRedirect(baseUrl);
    }
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
