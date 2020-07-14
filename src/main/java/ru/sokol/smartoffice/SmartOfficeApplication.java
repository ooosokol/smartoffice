package ru.sokol.smartoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartOfficeApplication.class, args);
    }

}
