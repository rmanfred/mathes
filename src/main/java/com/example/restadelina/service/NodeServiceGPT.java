package com.example.restadelina.service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class NodeServiceGPT {

    public static void main(String[] args) {
        var teams = new ArrayList<>(List.of("IR", "IT", "FR", "W", "ST", "EN"));
        var matches = generateMatches(teams);

        Queue<MyNode> frontier = new ArrayDeque<>();
        MyNode currentNode = new MyNode();

        frontier.add(currentNode);

        while (!frontier.isEmpty()) {
            currentNode = frontier.poll();
            System.out.println("size of frontier: " + frontier.size());

            if (isGoalMet(currentNode)) {
                System.out.println("Goal met!");
                printSchedule(currentNode);
                return;
            }

            for (Pair<String, String> match : matches) {
                for (int roundIndex = 0; roundIndex < 10; roundIndex++) {
                    for (int matchIndex = 0; matchIndex < 3; matchIndex++) {
                        MyNode node = buildNode(currentNode, match, roundIndex, matchIndex);
                        if (node != null) {
                            frontier.add(node);
                        }
                    }
                }
            }
        }

        System.out.println("No solution found.");
    }

    private static List<Pair<String, String>> generateMatches(List<String> teams) {
        List<Pair<String, String>> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matches.add(Pair.of(teams.get(i), teams.get(j)));
                matches.add(Pair.of(teams.get(j), teams.get(i)));
            }
        }
        return matches;
    }

    public static MyNode buildNode(MyNode node, Pair<String, String> match, int roundIndex, int matchIndex) {
        MyNode newNode = new MyNode();

        newNode.getRounds().putAll(node.getRounds());

        // Check constraints before adding the match
        if (!isMatchRepeated(newNode, match, roundIndex) && !isMatchTwiceInARow(newNode, match, roundIndex)) {
            Map<Integer, Pair<String, String>> round = newNode.getRounds().computeIfAbsent(roundIndex, k -> new HashMap<>());
            round.put(matchIndex, match);
            return newNode;
        }

        return null; // Return null if constraints are violated
    }

    private static boolean isMatchRepeated(MyNode node, Pair<String, String> match, int roundIndex) {
        Map<Integer, Pair<String, String>> round = node.getRounds().get(roundIndex);
        if (round != null) {
            for (Pair<String, String> existingMatch : round.values()) {
                if (existingMatch.getLeft().equals(match.getLeft()) || existingMatch.getLeft().equals(match.getRight()) ||
                        existingMatch.getRight().equals(match.getLeft()) || existingMatch.getRight().equals(match.getRight())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isMatchTwiceInARow(MyNode node, Pair<String, String> match, int roundIndex) {
        if (roundIndex > 0) {
            Map<Integer, Pair<String, String>> prevRound = node.getRounds().get(roundIndex - 1);
            if (prevRound != null) {
                for (Pair<String, String> existingMatch : prevRound.values()) {
                    if (existingMatch.getLeft().equals(match.getLeft()) && existingMatch.getRight().equals(match.getRight())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isGoalMet(MyNode node) {
        if (node.getRounds().size() == 1) {
            for (Map<Integer, Pair<String, String>> round : node.getRounds().values()) {
                if (round.size() != 3) {
                    return false; // Each team must play exactly 3 matches in each round
                }
            }
            return true;
        }
        return false; // 10 rounds must be scheduled
    }

    private static void printSchedule(MyNode node) {
        for (Map.Entry<Integer, Map<Integer, Pair<String, String>>> roundEntry : node.getRounds().entrySet()) {
            int roundIndex = roundEntry.getKey();
            Map<Integer, Pair<String, String>> matches = roundEntry.getValue();
            System.out.println("Round " + (roundIndex + 1) + ":");
            for (Map.Entry<Integer, Pair<String, String>> matchEntry : matches.entrySet()) {
                int matchIndex = matchEntry.getKey();
                Pair<String, String> match = matchEntry.getValue();
                System.out.println("Match " + (matchIndex + 1) + ": " + match.getLeft() + " vs " + match.getRight());
            }
        }
    }
}

class MyNode {
    private final Map<Integer, Map<Integer, Pair<String, String>>> rounds = new HashMap<>();

    public Map<Integer, Map<Integer, Pair<String, String>>> getRounds() {
        return rounds;
    }
}


