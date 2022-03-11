package cn.icodening.example.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;

/**
 * @author icodening
 * @date 2022.03.11
 */
@SpringBootApplication(exclude = {ServletWebServerFactoryAutoConfiguration.class})
public class MultipleServletContainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultipleServletContainerApplication.class);
    }
}
