package org.example.exchangeratesservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.exchangeratesservice.converter.CurrencyDTOConverter;
import org.example.exchangeratesservice.dto.CurrencyDTO;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.example.exchangeratesservice.repo.CurrencyRateRepo;
import org.example.exchangeratesservice.service.CurrencyRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepo currencyRateRepo;
    private final CurrencyDTOConverter converter;

    @Override
    public ResponseEntity<String> loadCurrencyRatesByDate(LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.nbrb.by/exrates/rates?ondate=" + date + "&periodicity=0";
        System.out.println(url);
        try {
            CurrencyDTO[] rates = restTemplate.getForObject(url, CurrencyDTO[].class);
            System.out.println(Arrays.toString(restTemplate.getForObject(url, CurrencyDTO[].class)));
            assert rates != null;
            for (CurrencyDTO rate : rates) {
                System.out.println(rate);
                currencyRateRepo.save(converter.convertCurrencyDTOToCurrencyRate(rate));
            }
            return ResponseEntity.ok("Data for " + date.toString() + " loaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load data for " + date.toString());
        }
    }

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
            System.out.println(rate);
            CurrencyRate exchangeRate = converter.convertCurrencyDTOToCurrencyRate(rate);
            currencyRateRepo.save(exchangeRate);
            return exchangeRate;
        }
    }
}
