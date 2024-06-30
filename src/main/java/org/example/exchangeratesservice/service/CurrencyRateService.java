package org.example.exchangeratesservice.service;

import org.example.exchangeratesservice.model.CurrencyRate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface CurrencyRateService {

    ResponseEntity<String> loadCurrencyRatesByDate(LocalDate date);
    CurrencyRate getCurrencyRate(LocalDate date, Integer code);
}
