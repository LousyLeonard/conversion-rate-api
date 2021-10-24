package com.example.demo.controllers;

import com.example.demo.models.ExchangeRatesAtDate;
import com.example.demo.services.ExchangeRateCSVProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/date")
public class ExchangeRateController {

    @Autowired
    ExchangeRateCSVProvider exchangeRatesProvider;

    /**
     * Request all records for the time period designated.
     * <p>
     * HTTP.OK Given on good request, empty body means no results found.
     *
     * @param start - The ISO formatted date start date, without time - Optional, if omitted the start of time is used.
     * @param end   - The ISO formatted date start date, without time - Optional, if omitted the end of time is used.
     * @return the records in a JSON format.
     */
    @GetMapping()
    public ResponseEntity<List<ExchangeRatesAtDate>> getExchangeRatesForDateStartEnd(
            @RequestParam Optional<LocalDate> start, @RequestParam Optional<LocalDate> end) {
        return ResponseEntity.ok(exchangeRatesProvider.rates()
                .filter(x -> x.getDate().isAfter(start.orElse(LocalDate.MIN)))
                .filter(x -> x.getDate().isBefore(end.orElse(LocalDate.MAX)))
                .collect(Collectors.toList()));
    }
}