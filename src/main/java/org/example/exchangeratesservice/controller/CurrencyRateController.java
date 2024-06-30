package org.example.exchangeratesservice.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.example.exchangeratesservice.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Validated
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    // Pre download exchange rates for all currencies on a specific day if possible and return if download was successful
    @GetMapping("load-exrates")
    public ResponseEntity<String> loadCurrencyRatesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull @PastOrPresent LocalDate date) {
        return currencyRateService.loadCurrencyRatesByDate(date);
    }

    // Returns the exchange rate to a specific currency on a specific day
    @GetMapping("get-exrate")
    public ResponseEntity<CurrencyRate> getCurrencyRate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull @PastOrPresent LocalDate date,
                                                        @NotNull(message = "Код валюты не введён") @Min(value = 1, message = "Код валюты должен быть не меньше 1") Integer code) {
        return ResponseEntity.ok(currencyRateService.getCurrencyRate(date, code));
    }
}
