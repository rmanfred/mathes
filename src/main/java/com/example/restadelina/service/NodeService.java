package com.example.restadelina.service;

import com.example.restadelina.model.MyNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class NodeService {
    public static void main(String[] args) {
        var teams = new ArrayList<>(List.of("IR", "IT", "FR", "W", "ST", "EN"));
        var matches = new ArrayList<Pair<String, String>>();
        Queue<MyNode> frontier = new ArrayDeque<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matches.add(Pair.of(teams.get(i), teams.get(j)));
                matches.add(Pair.of(teams.get(j), teams.get(i)));
            }
        }

        for (Pair<String, String> match : matches) {
            System.out.println(match);
        }

        MyNode currentNode = new MyNode();
        frontier.add(currentNode);
        while (!isGoalMet(currentNode)) {
            System.out.println("frontier size: " + frontier.size());
            for (int j = 0; j < 10; j++) {
                currentNode = frontier.poll();
                for (int k = 0; k < 3; k++) {
                    for (int i = 0; i < matches.size(); i++) {
                        var node = buildNode(currentNode, matches.get(i), j, k);
                        if(isGoalMet(node)){
                            for (Map.Entry<Integer, Map<Integer, Pair<String, String>>> entry : node.getRounds().entrySet()) {
                                System.out.println("Round index: " + entry.getKey());
                                System.out.println("size of node: " + frontier.size());
                                for (Map.Entry<Integer, Pair<String, String>> insideEntry : entry.getValue().entrySet()) {
                                    System.out.println("Match index: " + insideEntry.getKey());
                                    System.out.println("Team1: " + insideEntry.getValue().getLeft());
                                    System.out.println("Team2: " + insideEntry.getValue().getRight());
                                    System.out.println("--------------------------------------------");
                                }
                            }
                            return;
                        }
                        frontier.add(node);
                    }
                }
            }

//            for (int i = 0; i < matches.size(); i++) {
//                for (int j = 0; j < 10; j++) {
//                    currentNode = frontier.poll();
//                    for (int k = 0; k < 3; k++) {
//                        var node = buildNode(currentNode, matches.get(i), j, k);
//                        if(isGoalMet(node)){
//                            for (Map.Entry<Integer, Map<Integer, Pair<String, String>>> entry : node.getRounds().entrySet()) {
//                                System.out.println("Round index: " + entry.getKey());
//                                for (Map.Entry<Integer, Pair<String, String>> insideEntry : entry.getValue().entrySet()) {
//                                    System.out.println("Match index: " + insideEntry.getKey());
//                                    System.out.println("Team1: " + insideEntry.getValue().getLeft());
//                                    System.out.println("Team2: " + insideEntry.getValue().getRight());
//                                }
//                            }
//                            return;
//                        }
//                        frontier.add(node);
//                    }
//                }
//            }
        }
    }

    public static MyNode buildNode(MyNode node, Pair<String, String> match, int roundIndex, int matchIndex) {
        MyNode newNode = new MyNode();

        newNode.getRounds().putAll(node.getRounds());
        var round = newNode.getRounds().get(roundIndex);
        if (round != null) {
            var isNew = true;
            for (Map.Entry<Integer, Pair<String, String>> entryMatch : round.entrySet()) {
                if (entryMatch.getValue().equals(match) || entryMatch.getValue().getLeft().equals(match.getLeft())
                        || entryMatch.getValue().getRight().equals(match.getLeft())
                        || entryMatch.getValue().getLeft().equals(match.getRight())
                        || entryMatch.getValue().getRight().equals(match.getRight())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew && !isMatchRepeated(newNode, match, roundIndex)) {
                round.putIfAbsent(matchIndex, match);
            }
        } else {
            Map<Integer, Pair<String, String>> newRound = new HashMap<>();
            if (!isMatchRepeated(newNode, match, roundIndex)) {
                newRound.putIfAbsent(matchIndex, match);
                newNode.getRounds().put(roundIndex, newRound);
            }
        }
        return newNode;
    }

    private static boolean isMatchRepeated(MyNode node, Pair<String, String> match, int roundIndex) {
        var previousRound = node.getRounds().get(roundIndex - 1);
        if (previousRound != null) {
            for (Map.Entry<Integer, Pair<String, String>> matchEntry : previousRound.entrySet()) {
                if (matchEntry.getValue().getLeft().equals(match.getLeft())
                        || matchEntry.getValue().getRight().equals(match.getRight())) {
                    return true;
                }
            }
        }

        for (Map.Entry<Integer, Map<Integer, Pair<String, String>>> roundEntry : node.getRounds().entrySet()) {
            for (Map.Entry<Integer, Pair<String, String>> matchEntry : roundEntry.getValue().entrySet()) {
                if (matchEntry.getValue().equals(match)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isGoalMet(MyNode node) {
        if (node.getRounds().size() == 3) {
            for (Map.Entry<Integer, Map<Integer, Pair<String, String>>> entry : node.getRounds().entrySet()) {
                var match = entry.getValue();
                if (match.size() != 3) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
