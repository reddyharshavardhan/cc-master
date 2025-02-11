package com.example.currency_converter.controller;

import com.example.currency_converter.service.CurrencyService;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    public Map<String, Object> getRates(@RequestParam(value = "base", defaultValue = "USD") String baseCurrency) {
        return currencyService.getExchangeRates(baseCurrency);
    }

    @PostMapping("/convert")
    public Map<String, Object> convertCurrency(@RequestBody Map<String, Object> request) {
        String from = (String) request.get("from");
        String to = (String) request.get("to");
        Double amount = Double.valueOf(request.get("amount").toString());

        Double convertedAmount = currencyService.convert(from, to, amount);

        // Use DecimalFormat to format the convertedAmount to 2 decimal places
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedAmount = df.format(convertedAmount);

        // Use LinkedHashMap to preserve the insertion order
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("from", from);
        response.put("to", to);
        response.put("amount", amount);
        response.put("convertedAmount", formattedAmount);
        return response;
    }
}
