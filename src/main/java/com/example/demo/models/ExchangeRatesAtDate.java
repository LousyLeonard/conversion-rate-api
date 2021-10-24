package com.example.demo.models;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.apache.commons.collections4.MultiValuedMap;

import java.time.LocalDate;

public class ExchangeRatesAtDate {

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "Date")
    private LocalDate date;

    @CsvBindAndJoinByName(column = ".*", elementType = String.class, mapType = MultiValuedMap.class)
    private MultiValuedMap<String, String> rates;

    public LocalDate getDate() {
        return date;
    }

    public MultiValuedMap<String, String> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "ExchangeRateAtDate{" +
                "date=" + date +
                ", rates=" + rates +
                '}';
    }
}