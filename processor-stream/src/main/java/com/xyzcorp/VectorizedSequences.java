package com.xyzcorp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VectorizedSequences {

    private final Function<String, Optional<Integer>> function;
    private final int size;

    public VectorizedSequences(Function<String, Optional<Integer>> function,
                               int size) {
        this.function = function;
        this.size = size;
    }

    public List<Double> vectorize(String s) {
        if (size == 0) return List.of();

        List<Double> result =
            Stream.generate(() -> 0.0)
                  .limit(size)
                  .collect(Collectors.toList());

        Arrays.stream(s.split(" "))
              .forEach(w ->
                  function.apply(w).ifPresent(i -> result.set(i, 1.0)));
        return result;
    }
}
