package com.xyzcorp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONConverterTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void initializeObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    //Component 1: a. Take List<Integer> into JSON
    //             b. Take Json to List<Integer>
    //Component 2: Convert JSON Response into Category
    //Component 3: HTTP Client Processor

    @Test
    public void testRequestIntoJSON() throws IOException {
        List<Double> content = List.of(1.0, 0.0, 1.0);

        JSONConverter jsonConverter = new JSONConverter(objectMapper,
            x -> Optional.empty());
        String result = jsonConverter.convertRequestListToJson(content);

        TypeReference<Map<String, List<Integer>>> mapTypeReference =
            new TypeReference<>() {
        };

        assertThat(objectMapper.readValue(result, mapTypeReference))
            .isEqualTo(Map.of("instances", content));
    }

    @Test
    public void testResponseIntoStringForBronze() throws JsonProcessingException {
        String response = "{\n" +
            "  \"predictions\": [\n" +
            "    [\n" +
            "      0.02,\n" +
            "      0.01,\n" +
            "      3.00\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

        Map<Integer, String> lookupTable = Map.of(0, "Gold", 1, "Silver", 2,
            "Bronze");
        Function<Integer, Optional<String>> f =
            x -> Optional.ofNullable(lookupTable.get(x));
        JSONConverter jsonConverter = new JSONConverter(objectMapper, f);
        String result = jsonConverter.convertResponseToCategory(response);
        assertThat(result).isEqualTo("Bronze");
    }

    @Test
    public void testResponseIntoStringForGold() throws JsonProcessingException {
        String response = "{\n" +
            "  \"predictions\": [\n" +
            "    [\n" +
            "      4.02,\n" +
            "      0.01,\n" +
            "      3.00\n" +
            "    ]\n" +
            "  ]\n" +
            "}\n";

        Map<Integer, String> lookupTable = Map.of(0, "Gold", 1, "Silver", 2,
            "Bronze");
        Function<Integer, Optional<String>> f =
            x -> Optional.ofNullable(lookupTable.get(x));
        JSONConverter jsonConverter = new JSONConverter(objectMapper, f);
        String result = jsonConverter.convertResponseToCategory(response);
        assertThat(result).isEqualTo("Gold");
    }
}
