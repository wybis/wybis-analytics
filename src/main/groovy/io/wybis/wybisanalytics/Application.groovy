package io.wybis.wybisanalytics

import io.wybis.wybisanalytics.service.AccessDataService
import io.wybis.wybisanalytics.service.impl.DefaultAccessDataService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    File appHome() {
        File appHome = new File('c:/myhome/wybis-analytics')

        return appHome;
    }
}
