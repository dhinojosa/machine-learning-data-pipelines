package com.xyzcorp;

import redis.clients.jedis.Jedis;

import java.util.Optional;

public class JedisOptionalDecorator {

    private final Jedis jedis;

    public JedisOptionalDecorator(Jedis jedis) {
        this.jedis = jedis;
    }

    public static JedisOptionalDecorator create() {
        Jedis redisHost = new Jedis(System.getenv("REDIS_HOST"));
        String redisPassword = redisHost.auth(System.getenv(
            "REDIS_PASSWORD"));
        System.out.println("REDIS-LOGIN Status: " + redisPassword);
        return new JedisOptionalDecorator(redisHost);
    }

    public Optional<String> getAsString(String key) {
        return Optional.ofNullable(jedis.get(key));
    }

    public Optional<Integer> getAsInt(String key) {
        return Optional.ofNullable(jedis.get(key)).flatMap(s -> {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException numberFormatException) {
                return Optional.empty();
            }
        });
    }
}
