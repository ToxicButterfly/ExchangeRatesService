package org.example.exchangeratesservice.controller;
import org.example.exchangeratesservice.model.CurrencyRate;
import org.example.exchangeratesservice.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyRateController.class)
public class ControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRateService currencyRateService;

    @Test
    void testLoadCurrencyRatesByDate() throws Exception {
        when(currencyRateService.loadCurrencyRatesByDate(any(LocalDate.class))).thenReturn(ResponseEntity.ok("Data loaded successfully"));

        mockMvc.perform(get("/api/v1/load-exrates")
                        .param("date", "2024-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Data loaded successfully"));
    }

    @Test
    void testLoadCurrencyRatesByDate_InvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/load-exrates")
                        .param("date", "2025-01-01") // future date for validation test
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCurrencyRate() throws Exception {
        CurrencyRate mockCurrencyRate = new CurrencyRate(1, "RUB", 3.4991, LocalDate.of(2024, 1, 1));
        when(currencyRateService.getCurrencyRate(any(LocalDate.class), any(Integer.class))).thenReturn(mockCurrencyRate);

        mockMvc.perform(get("/api/v1/get-exrate")
                        .param("date", "2024-01-01")
                        .param("code", "456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"curId\":1,\"curAbbreviation\":\"RUB\",\"curOfficialRate\":3.4991,\"date\":\"2024-01-01\"}"));
    }

    @Test
    void testGetCurrencyRate_InvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/get-exrate")
                        .param("date", "2025-01-01") // future date for validation test
                        .param("code", "456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCurrencyRate_InvalidCode() throws Exception {
        mockMvc.perform(get("/api/v1/get-exrate")
                        .param("date", "2024-01-01")
                        .param("code", "-1") // invalid code for validation test
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
