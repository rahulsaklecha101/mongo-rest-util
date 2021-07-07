package com.saklecha;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan("com.saklecha.entity")
public class MainApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }
}

