package org.example.exchangeratesservice.exception;

public class CurrencyExchangeNotFoundException extends Exception{
    public CurrencyExchangeNotFoundException(String message) {
        super(message);
    }
}
