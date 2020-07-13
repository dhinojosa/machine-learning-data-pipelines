package com.xyzcorp;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;

import java.util.List;
import java.util.Properties;

public class ProcessorStream {
    public static void main(String[] args) {
        String kafkaService = System.getenv("KAFKA_SERVICE");
        String kafkaPort = System.getenv("KAFKA_PORT");
        System.out.println(kafkaService);
        if (kafkaPort == null) kafkaPort = "9092";

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,
            "my_streams_app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            kafkaService + ":" + kafkaPort);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
            Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
            Serdes.String().getClass());

        System.out.println("Starting Stream");

        JedisOptionalDecorator jedisOptionalDecorator =
            JedisOptionalDecorator.create();

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream =
            builder.stream("newsfeed");

        VectorizedSequences vs =
            new VectorizedSequences(jedisOptionalDecorator::getAsInt, 10000);

        TensorFlowNegotiator tensorFlowNegotiator =
            TensorFlowNegotiator.fromEnvVariables();

        stream.mapValues(v -> new KeyValue<String, List<Double>>(v, vs.vectorize(v)))
              .peek((k,v) -> System.out.format("Pipe 1: (%s, %s)", k, v.toString()))
              .map((k, v) -> new KeyValue<String, String>(tensorFlowNegotiator.send(v.value), v.key))
              .peek((k,v) -> System.out.format("Pipe 2: (%s, %s)", k, v))
              .to("categoried-newsfeed");

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);

        streams.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
