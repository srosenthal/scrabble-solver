package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * A simple solver - brute force, loop over all words in the dictionary, filter to the ones that can work, sort by point
 * value, and return the top matches. This works, but in the worst case can require sorting the whole dictionary, for
 * an input of a long string of wildcard characters.
 */
public class SolverV1 implements Solver {
    private final Dictionary dictionary;
    private final Scorer scorer;

    public SolverV1(Dictionary dictionary, Scorer scorer) {
        this.dictionary = dictionary;
        this.scorer = scorer;
    }

    @Override
    public Collection<ScoredWord> getMatches(String rack, int limit) {
        return dictionary.getWords().stream()
                .filter(word -> isMatch(rack, word))
                .map(word -> new ScoredWord(word, scorer.computeValue(word)))
                .sorted(Comparator.comparing(match -> -match.getPointValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static boolean isMatch(String rack, String word) {
        StringBuilder remainingRack = new StringBuilder(rack);

        for (char letter : word.toCharArray()) {
            int letterIndex = remainingRack.indexOf(Character.toString(letter));
            if (letterIndex >= 0) {
                // Found a matching letter.
                remainingRack.deleteCharAt(letterIndex);
            } else {
                // Could not find the letter. Try a wildcard.
                int wildcardIndex = remainingRack.indexOf(Character.toString(WILDCARD_CHAR));
                if (wildcardIndex >= 0) {
                    // Used a wildcard to match this letter.
                    remainingRack.deleteCharAt(wildcardIndex);
                } else {
                    // Not a match!
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "SolverV1{}";
    }
}
