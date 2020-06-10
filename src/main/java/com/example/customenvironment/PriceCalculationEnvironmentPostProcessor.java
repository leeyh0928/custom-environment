package com.example.customenvironment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

@Slf4j
@Order
public class PriceCalculationEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String PREFIX = "com.custom.environmentpostprocessor.";
    private static final String CALCUATION_MODE = "calculation_mode";
    private static final String GROSS_CALCULATION_TAX_RATE = "gross_calculation_tax_rate";
    private static final String CALCUATION_MODE_DEFAULT_VALUE = "NET";
    private static final double GROSS_CALCULATION_TAX_RATE_DEFAULT_VALUE = 0;

    List<String> names = Arrays.asList(CALCUATION_MODE, GROSS_CALCULATION_TAX_RATE);

    private static Map<String, Object> defaults = new LinkedHashMap<>();
    static {
        defaults.put(CALCUATION_MODE, CALCUATION_MODE_DEFAULT_VALUE);
        defaults.put(GROSS_CALCULATION_TAX_RATE, GROSS_CALCULATION_TAX_RATE_DEFAULT_VALUE);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PropertySource<?> system = environment.getPropertySources()
                .get(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);

        Map<String, Object> prefixed;
        if (!hasOurPriceProperties(Objects.requireNonNull(system))) {
            log.warn("System environment variables [calculation_mode,gross_calculation_tax_rate] not detected, fallback to default value [calcuation_mode={},gross_calcuation_tax_rate={}]",
                    CALCUATION_MODE_DEFAULT_VALUE, GROSS_CALCULATION_TAX_RATE_DEFAULT_VALUE);

            prefixed = names.stream()
                    .collect(Collectors.toMap(this::rename, this::getDefaultValue));

            environment.getPropertySources()
                    .addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new MapPropertySource("prefixer", prefixed));
            return;
        }

        prefixed = names.stream()
                .collect(Collectors.toMap(this::rename, system::getProperty));

        environment.getPropertySources()
                .addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new MapPropertySource("prefixer", prefixed));
    }

    private Object getDefaultValue(String key) {
        return defaults.get(key);
    }

    private String rename(String key) {
        return PREFIX + key.replaceAll("\\_", ".");
    }

    private boolean hasOurPriceProperties(PropertySource<?> system) {
        if (system.containsProperty(CALCUATION_MODE) && system.containsProperty(GROSS_CALCULATION_TAX_RATE)) {
            return true;
        } else
            return false;
    }
}
