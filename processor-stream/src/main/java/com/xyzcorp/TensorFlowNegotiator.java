package com.xyzcorp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Function;

public class TensorFlowNegotiator {

    private final HttpClient httpClient;
    private final String version;
    private final Function<List<Double>, String> convertToJson;
    private final String host;
    private final Function<String, String> convertResponseToCategory;
    private final String port;

    public TensorFlowNegotiator(String host,
                                String port,
                                String version,
                                Function<List<Double>, String> convertToJson,
                                Function<String, String> convertResponseToCategory,
                                HttpClient httpClient) {
        this.host = host;
        this.port = port;
        this.version = version;
        this.convertToJson = convertToJson;
        this.convertResponseToCategory = convertResponseToCategory;
        this.httpClient = httpClient;
    }

    public static TensorFlowNegotiator fromEnvVariables()  {
        String url = System.getenv("TENSOR_FLOW_SERVICE");
        String port = System.getenv("TENSOR_FLOW_SERVING_PORT");
        String version = System.getenv("TENSOR_FLOW_MODEL_VERSION");
        JSONConverter jsonConverter = JSONConverter.create();
        HttpClient httpClient = HttpClient.newBuilder().build();
        return new TensorFlowNegotiator(url, port, version,
            jsonConverter::convertRequestListToJson,
            jsonConverter::convertResponseToCategory,
            httpClient);
    }

    public String send(List<Double> data) {
        String str =
            String.format(
               "http://%s:%s/v1/models/reuters_model/versions/%s:predict", host, port, version);

        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(str))
            .POST(HttpRequest.BodyPublishers.ofString(convertToJson.apply(data)))
            .build();

        System.out.println("Sending content");

        try {
            HttpResponse<String> httpResponse = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
            System.out.println("Received Body" + httpResponse);
            return convertResponseToCategory.apply(httpResponse.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
