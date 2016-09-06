package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A more complex solver, which is ~2x faster than the simple brute-force solver and ~50% faster than the pre-sorted
 * solver.
 *
 * This one has a more complicated algorithm for determining if a word is a match. The rack is sorted to
 * alphabetical order (wildcards last). Then for each letter in the candidate word, a binary search is applied to the
 * rack to find the required letter. If the letter exists in the rack, it is removed and the algorithm continues with
 * the remaining letters. If a letter could not be found, a wildcard is removed (if one exists) or it the word is not a
 * match. Best case: large racks, matches found early. Worst case: matches not found, so full dictionary is traversed.
 */
public class SolverV3 implements Solver {
    private final List<ScoredWord> sortedWords;

    public SolverV3(Dictionary dictionary, Scorer scorer) {
        this.sortedWords = dictionary.getWords().stream()
                .map(word -> new ScoredWord(word, scorer.computeValue(word)))
                .sorted(Comparator.comparing(match -> -match.getPointValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ScoredWord> getMatches(String rack, int limit) {
        char[] sortedRack = rack.toCharArray();
        Arrays.sort(sortedRack);

        return sortedWords.stream()
                .filter(candidate -> isMatch(sortedRack, candidate.getWord()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private static boolean isMatch(char[] sortedRack, String word) {
        char[] remainingRack = Arrays.copyOf(sortedRack, sortedRack.length);

        char[] letters = word.toCharArray();

        for (char letter : letters) {
            if (remainingRack.length == 0) {
                // Can't possibly match, no letters left in the rack.
                return false;
            }

            int index = Arrays.binarySearch(remainingRack, letter);
            if (index >= 0) {
                // Found a matching letter.
                char[] newRemainingRack = new char[remainingRack.length - 1];
                System.arraycopy(remainingRack, 0, newRemainingRack, 0, index );
                System.arraycopy(remainingRack, index+1, newRemainingRack, index, remainingRack.length - index-1);
                remainingRack = newRemainingRack;
            } else {
                // Could not find the letter. Try a wildcard.
                if (remainingRack[remainingRack.length - 1] == WILDCARD_CHAR) {
                    // Used a wildcard to match this letter.
                    remainingRack = Arrays.copyOf(remainingRack, remainingRack.length - 1);
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
        return "SolverV3{}";
    }
}
