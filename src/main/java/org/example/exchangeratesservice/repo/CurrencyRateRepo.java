package org.example.exchangeratesservice.repo;

import org.example.exchangeratesservice.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateRepo extends JpaRepository<CurrencyRate, Integer> {

    Optional<CurrencyRate> findCurrencyRateByDateAndCurId(LocalDate date, Integer cur_id);
}
