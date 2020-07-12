package com.xyzcorp;

import java.util.Objects;
import java.util.StringJoiner;

class Tuple2<A, B> {
    private final A a;
    private final B b;

    public Tuple2(A a, B b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return a.equals(tuple2.a) &&
            b.equals(tuple2.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tuple2.class.getSimpleName() + "[",
            "]")
            .add("a=" + a)
            .add("b=" + b)
            .toString();
    }
}
