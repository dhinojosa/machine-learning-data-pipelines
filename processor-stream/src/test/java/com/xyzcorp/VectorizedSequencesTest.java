package com.xyzcorp;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorizedSequencesTest {
    @Test
    public void testEmptyKeyValue() {
        Map<String, Integer> map = Map.of();
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("");
        assertThat(result).isEqualTo(List.of());
    }

    @Test
    public void testOneLetterSentence() {
        Map<String, Integer> map = Map.of("clown", 0);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("clown");
        assertThat(result).isEqualTo(List.of(1));
    }

    @Test
    public void testTwoLetterSentenceWhereBothAreInMap() {
        Map<String, Integer> map = Map.of("clown", 0, "butter", 1);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("clown butter");
        assertThat(result).isEqualTo(List.of(1, 1));
    }

    @Test
    public void testTwoLetterSentenceWhereBothAreInAThreeElementMap() {
        Map<String, Integer> map = Map.of("clown", 0, "butter", 1, "zoom", 2);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("clown butter");
        assertThat(result).isEqualTo(List.of(1, 1, 0));
    }

    @Test
    public void testTwoLetterSentenceWhereBothAreInFourElementMap() {
        Map<String, Integer> map = Map.of("clown", 0, "butter", 1, "zoom", 2,
            "bowler", 3);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("clown butter");
        assertThat(result).isEqualTo(List.of(1, 1, 0, 0));
    }

    @Test
    public void testTwoLetterSentenceWhereOneAreInFourElementMap() {
        Map<String, Integer> map = Map.of("clown", 0, "butter", 1, "zoom", 2,
            "bowler", 3);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("clown eclair");
        assertThat(result).isEqualTo(List.of(1, 0, 0, 0));
    }

    @Test
    public void testFiveLetterSentenceWhereNoneAreInFourElementMap() {
        Map<String, Integer> map = Map.of("clown", 0, "butter", 1, "zoom", 2,
            "bowler", 3);
        Function<String, Optional<Integer>> getValueOption =
            x -> Optional.ofNullable(map.get(x));
        VectorizedSequences vectorizedSequences =
            new VectorizedSequences(getValueOption, map.size());
        List<Double> result = vectorizedSequences.vectorize("the vector is vulgar");
        assertThat(result).isEqualTo(List.of(0, 0, 0, 0));
    }
}
