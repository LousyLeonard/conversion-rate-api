package com.example.demo.controllers;

import com.example.demo.services.ExchangeRateCSVProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/stats")
public class ExchangeStatsController {

    @Autowired
    ExchangeRateCSVProvider exchangeRatesProvider;

    /**
     * Request the maximum conversion rate between the start and end dates (ISO format) and a currency character code.
     * (characters codes as given in https://www.ecb.europa.eu/stats/policy_and_exchange_rates/
     * euro_reference_exchange_rates/html/index.en.html) and finally the amount.
     * No care is given to format the response.
     * <p>
     * HTTP.OK and results given on good request.
     * HTTP.OK with no result means the server found no data for the dates and currencies supplied.
     *
     * @param start    - The ISO formatted date start date, without time - Optional, if omitted the start of time is used.
     * @param end      - The ISO formatted date start date, without time - Optional, if omitted the end of time is used.
     * @param currency - Three character currency identifier.
     * @return the pure numerical value converted as type double.
     */
    @RequestMapping("/max")
    public ResponseEntity<Double> getMaxExchangeRatesForDateStartEnd(@RequestParam Optional<LocalDate> start,
                                                                     @RequestParam Optional<LocalDate> end, @RequestParam String currency) {
        try {
            final Optional<Double> max = exchangeRatesProvider.rates()
                    .filter(x -> x.getDate().isAfter(start.orElse(LocalDate.MIN)))
                    .filter(x -> x.getDate().isBefore(end.orElse(LocalDate.MAX)))
                    .flatMap(x -> x.getRates().asMap().entrySet().stream().filter(
                            e -> e.getKey().equals(currency)
                    ))
                    .flatMap(x -> x.getValue().stream().map(e -> Double.parseDouble(e)))
                    .max((o1, o2) -> o1.compareTo(o2));

            return ResponseEntity.status(HttpStatus.OK).body(max.orElse(null));
        } catch (final NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    /**
     * Request the average conversion rate between the start and end dates (ISO format) and a currency character code.
     * (characters codes as given in https://www.ecb.europa.eu/stats/policy_and_exchange_rates/
     * euro_reference_exchange_rates/html/index.en.html) and finally the amount.
     * No care is given to format the response.
     * <p>
     * HTTP.OK and results given on good request.
     * HTTP.OK with no result means the server found no data for the dates and currencies supplied.
     *
     * @param start    - The ISO formatted date start date, without time - Optional, if omitted the start of time is used.
     * @param end      - The ISO formatted date start date, without time - Optional, if omitted the end of time is used.
     * @param currency - Three character currency identifier.
     * @return the pure numerical value converted as type double.
     */
    @RequestMapping("/average")
    public ResponseEntity<Double> getAverageExchangeRatesForDateStartEnd(@RequestParam Optional<LocalDate> start,
                                                                         @RequestParam Optional<LocalDate> end, @RequestParam String currency) {
        try {
            final OptionalDouble average = exchangeRatesProvider.rates()
                    .filter(x -> x.getDate().isAfter(start.orElse(LocalDate.MIN)))
                    .filter(x -> x.getDate().isBefore(end.orElse(LocalDate.MAX)))
                    .flatMap(x -> x.getRates().asMap().entrySet().stream().filter(
                            e -> e.getKey().equals(currency)
                    ))
                    .flatMap(x -> x.getValue().stream().map(e -> Double.parseDouble(e)))
                    .mapToDouble(x -> x)
                    .average();

            return ResponseEntity.status(HttpStatus.OK).body(average.getAsDouble());
        } catch (final NumberFormatException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}