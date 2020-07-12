package com.xyzcorp;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class JedisOptionalDecoratorTest {

    @Test
    public void testJediOptionalGetAsStringSuccess() {
        Jedis jedis = createMock(Jedis.class);
        JedisOptionalDecorator decorator = new JedisOptionalDecorator(jedis);
        expect(jedis.get("foo")).andReturn("bar");
        replay(jedis);
        Optional<String> result = decorator.getAsString("foo");
        assertThat(result).contains("bar");
        verify(jedis);
    }

    @Test
    public void testJediOptionalGetAsStringFailure() {
        Jedis jedis = createMock(Jedis.class);
        JedisOptionalDecorator decorator = new JedisOptionalDecorator(jedis);
        expect(jedis.get("foo")).andReturn(null);
        replay(jedis);
        Optional<String> result = decorator.getAsString("foo");
        assertThat(result).isEmpty();
        verify(jedis);
    }

    @Test
    public void testJediOptionalGetAsIntegerSuccess() {
        Jedis jedis = createMock(Jedis.class);
        JedisOptionalDecorator decorator = new JedisOptionalDecorator(jedis);
        expect(jedis.get("foo")).andReturn("32");
        replay(jedis);
        Optional<Integer> result = decorator.getAsInt("foo");
        assertThat(result).contains(32);
        verify(jedis);
    }

    @Test
    public void testJediOptionalGetAsIntFailure() {
        Jedis jedis = createMock(Jedis.class);
        JedisOptionalDecorator decorator = new JedisOptionalDecorator(jedis);
        expect(jedis.get("foo")).andReturn("bar");
        replay(jedis);
        Optional<Integer> result = decorator.getAsInt("foo");
        assertThat(result).isEmpty();
        verify(jedis);
    }
}
