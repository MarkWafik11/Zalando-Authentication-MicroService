package com.example.demo.customer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class CustomerConfig {
    @Bean
    CommandLineRunner commandLineRunner1(CustomerRepository repository){
        return args -> {
            Customer mario = new Customer(
                    "Mario",
                    "20 ElNozha Street",
                    "mario@gmail.com",
                    "01273615172",
                    LocalDate.of(2000, Month.JULY, 31)
            );

            Customer keez = new Customer(
                    "Kiro",
                    "16 ElNozha Street",
                    "kiro@gmail.com",
                    "01273615175",
                    LocalDate.of(1999, Month.MARCH, 9)
            );

            repository.saveAll(
                    List.of(mario, keez)
            );
        };
    }
}
