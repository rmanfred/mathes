package com.example.restadelina.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class MyNode {
    private long depth;
    private final Map<Integer, Map<Integer, Pair<String, String>>> rounds = new HashMap<>();

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public Map<Integer, Map<Integer, Pair<String, String>>> getRounds() {
        return rounds;
    }
}
