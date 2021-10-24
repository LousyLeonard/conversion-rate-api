package com.example.demo.controllers;

import com.example.demo.services.ExchangeRateCSVProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/convert")
public class ExchangeConverterController {

    @Autowired
    ExchangeRateCSVProvider exchangeRatesProvider;

    /**
     * Request a conversion between the date (ISO format) the currencyFrom and the currencyTo
     * (characters codes as given in https://www.ecb.europa.eu/stats/policy_and_exchange_rates/
     * euro_reference_exchange_rates/html/index.en.html) and finally the amount.
     * No care is given to format the response.
     * <p>
     * HTTP.OK and results given on good request.
     * HTTP.OK with no result means the server found no data for the dates and currencies supplied.
     * HTTP.UNPROCESSABLE_ENTITY given when the amount entered is not recognised. Please supply a number without
     * signage.
     *
     * @param date         - The ISO formatted date, without time.
     * @param currencyFrom - Three character currency identifier.
     * @param currencyTo   - Three character currency identifier.
     * @param amount       - Pure numerical value with no currency units. ($, £)
     * @return the pure numerical value converted as type double.
     */
    @GetMapping()
    public ResponseEntity<Double> getExchangeRatesForDateStartEnd(@RequestParam LocalDate date,
                                                                  @RequestParam String currencyFrom, @RequestParam String currencyTo, @RequestParam Double amount) {
        try {
            final List<Pair<String, Double>> rates = exchangeRatesProvider.rates()
                    .filter(x -> x.getDate().isEqual(date))
                    .flatMap(x -> x.getRates().asMap().entrySet().stream())
                    .filter(x -> x.getKey().equals(currencyFrom) || x.getKey().equals(currencyTo))
                    .map(x -> Pair.of(x.getKey(), x.getValue().stream().findFirst().orElseGet(() -> "")))
                    .map(x -> Pair.of(x.getKey(), Double.parseDouble(x.getRight())))
                    .collect(Collectors.toList());

            if (rates.size() == 2) {
                final Double fromRate = rates.stream()
                        .filter(x -> x.getKey().equals(currencyFrom)).findFirst().get().getValue();
                final Double toRate = rates.stream()
                        .filter(x -> x.getKey().equals(currencyTo)).findFirst().get().getValue();

                final Double finalAmount = amount / fromRate * toRate;

                return ResponseEntity.ok(finalAmount);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (final NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }
}