package com.example.customenvironment.calculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class NetPriceCalculator implements PriceCalculator {
    @Value("${com.custom.environmentpostprocessor.gross.calculation.tax.rate}")
    private double taxRate;

    @Override
    public double calculate(double singlePrice, int quantity) {
        log.info("Net based price calculation with input parameters [singlePrice = {},quantity= {} ], NO tax applied.",
                singlePrice, quantity);

        double netPrice = singlePrice * quantity;
        double result = Math.round(netPrice * (1 + taxRate));

        log.info("Calcuation result is {}", result);

        return result;
    }
}
