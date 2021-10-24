package com.example.demo.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.collections4.MultiValuedMap;

import java.io.IOException;

/**
 * Interception of the MultiValuesMap class serialisation to call asMap().
 * <p>
 * The MultiValuedMap is not in the Map heirarchy so we cannot rely on the map serialiser, but it can produce
 * a map from the usual java Map heirarchy is asMap() is called. Do that
 */
class MultiValuedMapJsonSerializer extends JsonSerializer<MultiValuedMap> {
    @Override
    public void serialize(MultiValuedMap value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeObject(value.asMap());
    }

    @Override
    public Class<MultiValuedMap> handledType() {
        return MultiValuedMap.class;
    }
}