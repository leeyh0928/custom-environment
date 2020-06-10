package com.example.customenvironment;

import com.example.customenvironment.service.PriceCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class CustomEnvironmentApplication implements CommandLineRunner {
    @Autowired
    PriceCalculationService priceCalculationService;

    public static void main(String[] args) {
        SpringApplication.run(CustomEnvironmentApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<String> params = Arrays.stream(args)
                .collect(Collectors.toList());
        if (verifyArguments(params)) {
            double singlePrice = Double.parseDouble(params.get(0));
            int quantity = Integer.parseInt(params.get(1));
            priceCalculationService.productTotalPrice(singlePrice, quantity);
        } else {
            log.warn("Invalid arguments " + params.toString());
        }
    }

    private boolean verifyArguments(List<String> args) {
        boolean successful = true;
        if (args.size() != 2) {
            successful = false;
            return successful;
        }
        try {
            double singlePrice = Double.parseDouble(args.get(0));
            int quantity = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            successful = false;
        }
        return successful;
    }
}
