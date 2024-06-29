package org.example.exchangeratesservice.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.example.exchangeratesservice.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping("load-exrates")
    public ResponseEntity<String> loadCurrencyRatesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return currencyRateService.loadCurrencyRatesByDate(date);
    }

    @GetMapping("get-exrate")
    public ResponseEntity<CurrencyRate> getCurrencyRate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                        Integer code) {
        return ResponseEntity.ok(currencyRateService.getCurrencyRate(date, code));
    }
}
