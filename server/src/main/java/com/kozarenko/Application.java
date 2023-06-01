package com.kozarenko;

import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.kozarenko.util.Constants.Cloudinary.CLOUDINARY_URL;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("CLOUDINARY_URL: " + CLOUDINARY_URL);
        SpringApplication.run(Application.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}