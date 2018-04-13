package fr.ftravaglia.demo.ihmdemocircuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class IhmDemoCircuitBreakerApplication {
    
    
    public static void main(String[] args) {
        SpringApplication.run(IhmDemoCircuitBreakerApplication.class, args);
    }

}
