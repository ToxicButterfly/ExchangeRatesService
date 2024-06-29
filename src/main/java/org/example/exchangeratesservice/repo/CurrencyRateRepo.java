package org.example.exchangeratesservice.repo;

import org.example.exchangeratesservice.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateRepo extends JpaRepository<CurrencyRate, Integer> {
}
