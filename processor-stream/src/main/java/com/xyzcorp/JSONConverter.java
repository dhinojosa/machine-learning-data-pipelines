package com.xyzcorp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class JSONConverter {
    private final Function<Integer, Optional<String>> categoryLookup;
    private final ObjectMapper objectMapper;

    public JSONConverter(ObjectMapper objectMapper,
                         Function<Integer, Optional<String>> categoryLookup) {
        this.objectMapper = objectMapper;
        this.categoryLookup = categoryLookup;
    }

    public static JSONConverter create() {
        Map<Integer, String> labels = LabelMapFactory.getLabels();
        Function<Integer, Optional<String>> f = x -> Optional.ofNullable(labels.get(x));
        return new JSONConverter(new ObjectMapper(), f);
    }

    public String convertRequestListToJson(List<Double> integers) {
        try {
            return objectMapper.writeValueAsString(Map.of("instances", List.of(integers)));
        } catch (JsonProcessingException e) {
            return "{\"instances\" : [[]]}";
        }
    }

    public String convertResponseToCategory(String response) {
        TypeReference<Map<String, List<List<Double>>>> typeReference =
            new TypeReference<>() {};
        Map<String, List<List<Double>>> map;

        try {
            map = objectMapper.readValue(response, typeReference);
        } catch (JsonProcessingException e) {
            return "Unknown";
        }

        List<Double> results = map.get("predictions").get(0);

        return IntStream.range(0, results.size())
                        .boxed()
                        .map(i -> new Tuple2<>(i, results.get(i)))
                        .max(Comparator.comparing(Tuple2::getB))
                        .map(Tuple2::getA)
                        .flatMap(categoryLookup)
                        .orElse("Unknown");
    }
}
