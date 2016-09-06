package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A more complex solver, which is ~50% faster than the simple brute-force solver.
 *
 * For this solver, the dictionary is pre-sorted by point value so that once the requested number of matches are found,
 * the result can be returned early, without a full traversal of the dictionary. Best case: the matches are found early.
 * Worst case: matches not found, so full dictionary is traversed..
 */
public class SolverV2  implements Solver {
    private final List<ScoredWord> sortedWords;

    public SolverV2(Dictionary dictionary, Scorer scorer) {
        this.sortedWords = dictionary.getWords().stream()
                .map(word -> new ScoredWord(word, scorer.computeValue(word)))
                .sorted(Comparator.comparing(match -> -match.getPointValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ScoredWord> getMatches(String rack, int limit) {
        return sortedWords.stream()
                .filter(candidate -> SolverV1.isMatch(rack, candidate.getWord()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "SolverV2{}";
    }
}
