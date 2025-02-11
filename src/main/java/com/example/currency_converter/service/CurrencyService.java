package com.example.currency_converter.service;

import com.example.currency_converter.exception.ExchangeRateNotAvailableException;
import com.example.currency_converter.exception.InvalidCurrencyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Service
public class CurrencyService {

    @Value("${openexchangerates.api.key}")
    private String apiKey;

    @Value("${openexchangerates.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getExchangeRates(String baseCurrency) {
        String url = apiUrl + "?app_id=" + apiKey + "&base=" + baseCurrency;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("rates")) {
            throw new ExchangeRateNotAvailableException("Exchange rates not available.");
        }

        return response;
    }

    public Double convert(String from, String to, Double amount) {
        Map<String, Object> response = getExchangeRates("USD");

        if (response.containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            Set<String> validCurrencies = rates.keySet(); // Get valid currency codes

            if (!validCurrencies.contains(from) || !validCurrencies.contains(to)) {
                throw new InvalidCurrencyException("Invalid currency codes: " + from + " or " + to);
            }

            Double fromRate = getDoubleValue(rates.get(from));
            Double toRate = getDoubleValue(rates.get(to));

            if (fromRate != null && toRate != null) {
                return (amount / fromRate) * toRate;
            }
        }

        throw new ExchangeRateNotAvailableException("Exchange rates not available.");
    }

    private Double getDoubleValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
}
