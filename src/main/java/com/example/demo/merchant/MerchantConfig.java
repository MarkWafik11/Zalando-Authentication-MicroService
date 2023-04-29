package com.example.demo.merchant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;

@Configuration
public class MerchantConfig {
    @Bean
    CommandLineRunner commandLineRunner2(MerchantRepository repository){
        return args -> {
            Merchant nike = new Merchant(
                    "Nike",
                    "nike@gmail.com",
                    "19991",
                    LocalDate.of(2000, Month.JULY, 31)
            );
            repository.save(nike);
        };
    }
}
