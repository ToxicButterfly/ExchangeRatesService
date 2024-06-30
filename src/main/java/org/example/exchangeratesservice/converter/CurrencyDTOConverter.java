package org.example.exchangeratesservice.converter;

import org.example.exchangeratesservice.dto.CurrencyDTO;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CurrencyDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public CurrencyRate convertCurrencyDTOToCurrencyRate(CurrencyDTO currencyDTO) {
        CurrencyRate currencyRate = modelMapper.map(currencyDTO, CurrencyRate.class);
        currencyRate.setDate(LocalDate.from(currencyDTO.getDate()));
        return currencyRate;
    }
}
