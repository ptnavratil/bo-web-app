package cz.upce.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Martin Volenec / st46661
 *
 * This web app uses feign cleints to communicate with REST APIs.
 * Registered as eureka discovery client.
 * Also enabled circuit breaker.
 */

@SpringBootApplication
// @EnableDiscoveryClient
@EnableFeignClients
public class Application
{
    public static void main(String[] args)
    {
        System.out.println("Pokus pro CircleCI");
        SpringApplication.run(Application.class, args);
    }
}
