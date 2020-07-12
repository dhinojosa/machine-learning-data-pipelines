package com.xyzcorp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpClientTest {

    @Test
    public void testHttpClient() throws IOException,
        InterruptedException {

        Map<Integer, String> labels = LabelMapFactory.getLabels();

        HttpClient client = HttpClient.newHttpClient();

        JSONConverter jsonConverter =
            new JSONConverter(new ObjectMapper(),
            x -> Optional.ofNullable(labels.get(x)));

        BufferedReader bufferedReader =
            new BufferedReader(
                new InputStreamReader(
                    getClass().getResourceAsStream("/sample-data2.json")));

        String collect1 = bufferedReader.lines().collect(Collectors.joining());

        HttpRequest request =
            HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8501/v1/models" +
                    "/reuters_model/versions/1594491302:predict"))
                .POST(HttpRequest.BodyPublishers.ofString(collect1))
                .build();

        System.out.println(jsonConverter.convertResponseToCategory(
            client.send(request, HttpResponse.BodyHandlers.ofString()).body()));
    }
}
