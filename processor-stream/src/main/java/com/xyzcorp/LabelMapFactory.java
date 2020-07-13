package com.xyzcorp;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LabelMapFactory {

    private static final List<String> listMap = List.of("cocoa", "grain", "veg-oil", "earn",
        "acq", "wheat", "copper", "housing", "money-supply",
        "coffee", "sugar", "trade", "reserves", "ship", "cotton",
        "carcass", "crude", "nat-gas",
        "cpi", "money-fx", "interest", "gnp", "meal-feed", "alum",
        "oilseed", "gold", "tin",
        "strategic-metal", "livestock", "retail", "ipi", "iron-steel",
        "rubber", "heat", "jobs",
        "lei", "bop", "zinc", "orange", "pet-chem", "dlr", "gas", "silver"
        , "wpi", "hog", "lead");

    public static Map<Integer, String> getLabels() {
        return IntStream.range(0, listMap.size())
                     .boxed()
                     .map(i -> Map.entry(i, listMap.get(i)))
                     .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
