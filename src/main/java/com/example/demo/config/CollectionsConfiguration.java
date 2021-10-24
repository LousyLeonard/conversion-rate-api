package com.example.demo.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CollectionsConfiguration {

    @Bean
    public Module multiMapDeserialiser() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(MultiValuedMap.class, new MultiValuedMapJsonSerializer());
        return module;
    }
}