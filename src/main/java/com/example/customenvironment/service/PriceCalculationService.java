package com.example.customenvironment.service;

import com.example.customenvironment.calculator.PriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {
    private final PriceCalculator priceCalculator;

    public double productTotalPrice(double singlePrice, int quantity) {
        return priceCalculator.calculate(singlePrice, quantity);
    }
}
