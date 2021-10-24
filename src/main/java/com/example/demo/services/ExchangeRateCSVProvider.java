package com.example.demo.services;

import com.example.demo.models.ExchangeRatesAtDate;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ExchangeRateCSVProvider implements InitializingBean {

    final Logger logger = LoggerFactory.getLogger(ExchangeRateCSVProvider.class);

    final Set<ExchangeRatesAtDate> inMemoryRates = new HashSet<>();

    public ExchangeRateCSVProvider() {
        // Do nothing
    }

    @Override
    public void afterPropertiesSet() {
        try {
            final Resource resource = new ClassPathResource("eurofxref-hist.csv");
            final FileReader fileReader = new FileReader(resource.getFile());
            final List<ExchangeRatesAtDate> rates = new CsvToBeanBuilder<ExchangeRatesAtDate>(fileReader)
                    .withType(ExchangeRatesAtDate.class)
                    .build()
                    .parse();
            this.inMemoryRates.addAll(rates);
            this.logger.info("CSV loaded successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<ExchangeRatesAtDate> rates() {
        return this.inMemoryRates.stream();
    }
}