package com.xyzcorp;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class ProcessorStream {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,
            "my_streams_app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
            Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
            Serdes.Integer().getClass());

        Jedis jedis = new Jedis(System.getenv("REDIS_HOST"));

        JedisOptionalDecorator jedisOptionalDecorator =
            new JedisOptionalDecorator(jedis);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream =
            builder.stream("newsfeed");

        VectorizedSequences vs =
            new VectorizedSequences(jedisOptionalDecorator::getAsInt, 10000);

        TensorFlowNegotiator tensorFlowNegotiator =
            TensorFlowNegotiator.fromEnvVariables();

        stream.mapValues(vs::vectorize)
              .map((k, v) -> new KeyValue<>(tensorFlowNegotiator.send(v), v))
              .to("categoried-newsfeed");

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);

        streams.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
