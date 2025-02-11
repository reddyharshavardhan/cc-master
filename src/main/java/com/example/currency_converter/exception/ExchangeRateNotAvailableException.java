package com.example.currency_converter.exception;

public class ExchangeRateNotAvailableException extends RuntimeException {
    public ExchangeRateNotAvailableException(String message) {
        super(message);
    }
}
