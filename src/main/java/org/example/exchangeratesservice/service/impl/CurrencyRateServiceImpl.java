package org.example.exchangeratesservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.exchangeratesservice.converter.CurrencyDTOConverter;
import org.example.exchangeratesservice.dto.CurrencyDTO;
import org.example.exchangeratesservice.exception.CurrencyExchangeNotFoundException;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.example.exchangeratesservice.repo.CurrencyRateRepo;
import org.example.exchangeratesservice.service.CurrencyRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepo currencyRateRepo;
    private final CurrencyDTOConverter converter;

    // Pre download exchange rates for all currencies on a specific day if possible and return if download was successful
    @Override
    public ResponseEntity<String> loadCurrencyRatesByDate(LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.nbrb.by/exrates/rates?ondate=" + date + "&periodicity=0";

        CurrencyDTO[] rates = restTemplate.getForObject(url, CurrencyDTO[].class);
        if (rates == null || rates.length == 0 || !rates[0].getDate().toLocalDate().equals(date)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load data for " + date.toString());
        }
        for (CurrencyDTO rate : rates) {
            currencyRateRepo.save(converter.convertCurrencyDTOToCurrencyRate(rate));
        }
        return ResponseEntity.ok("Data for " + date + " loaded successfully.");
    }

    // Returns the exchange rate to a specific currency on a specific day
    @SneakyThrows
    @Override
    public CurrencyRate getCurrencyRate(LocalDate date, Integer code) {
        Optional<CurrencyRate> exrate = currencyRateRepo.findCurrencyRateByDateAndCurId(date, code);
        if (exrate.isPresent()) {
            return exrate.get();
        }
        else {
            String url = "https://api.nbrb.by/exrates/rates/" + code + "?ondate=" + date;
            System.out.println(url);
            RestTemplate restTemplate = new RestTemplate();
            CurrencyDTO rate = restTemplate.getForObject(url, CurrencyDTO.class);
            if (rate == null || !rate.getDate().toLocalDate().equals(date)) {
                throw new CurrencyExchangeNotFoundException("Failed to load currency exchange");
            }
            CurrencyRate exchangeRate = converter.convertCurrencyDTOToCurrencyRate(rate);
            currencyRateRepo.save(exchangeRate);
            return exchangeRate;
        }
    }
}
