package org.example.msaccountreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsAccountReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAccountReservationApplication.class, args);
    }
}