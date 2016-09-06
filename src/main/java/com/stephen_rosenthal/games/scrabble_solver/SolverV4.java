package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Another solver, which is ~2.5x faster than the simple brute-force solver and ~20% faster than the pre-sorted
 * binary-search solver.
 *
 * This one pushes more computation up-front for more speed when computing matches. Each word is transformed into an
 * array of 26 integers representing counts by letter. When determining if a match is possible, a rack is transformed
 * into an array of the same format along with a count of the available wildcards. The arrays are iterated over in
 * unison and the wildcard counter is reduced if necessary. Best case: large racks, matches found early. Worst case:
 * matches not found, so full dictionary is traversed.
 */
public class SolverV4 implements Solver {
    private final List<SuperScoredWord> sortedSuperScoredWords;

    public SolverV4(Dictionary dictionary, Scorer scorer) {
        this.sortedSuperScoredWords = dictionary.getWords().stream()
                .map(word -> new SuperScoredWord(word, scorer.computeValue(word)))
                .sorted(Comparator.comparing(superScoredWord -> -superScoredWord.getScoredWord().getPointValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ScoredWord> getMatches(String rack, int limit) {
        int numWildcards;
        String noWildcardRack;
        {
            int wildcardCounter = 0;
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : rack.toCharArray()) {
                if (c == WILDCARD_CHAR) {
                    wildcardCounter++;
                } else {
                    stringBuilder.append(c);
                }
            }
            numWildcards = wildcardCounter;
            noWildcardRack = stringBuilder.toString();
        }
        int[] availableCounts = wordToCounts(noWildcardRack);

        return sortedSuperScoredWords.stream()
                .filter(candidate -> isMatch(availableCounts, numWildcards, candidate))
                .limit(limit)
                .map(SuperScoredWord::getScoredWord)
                .collect(Collectors.toList());
    }

    private static boolean isMatch(int[] availableCounts, int numWildcards, SuperScoredWord superWord) {
        int[] requiredCounts = superWord.getLetterCounts();

        for (int i = 0; i < 26; i++) {
            int deficit = requiredCounts[i] - availableCounts[i];
            if (deficit > 0) {
                if (numWildcards >= deficit) {
                    // Use one or more wildcards
                    numWildcards -= deficit;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static class SuperScoredWord {
        private final ScoredWord scoredWord;
        private final int[] letterCounts;

        public SuperScoredWord(String word, int value) {
            this.scoredWord = new ScoredWord(word, value);
            this.letterCounts = wordToCounts(word);
        }

        private ScoredWord getScoredWord() {
            return scoredWord;
        }

        public int[] getLetterCounts() {
            return letterCounts;
        }
    }

    private static int[] wordToCounts(String word) {
        int[] letterCounts = new int[26];
        for (char c : word.toCharArray()) {
            int index = c - 'A';
            if (index < 0) {
                System.out.println();
            }
            letterCounts[index] = letterCounts[index] + 1;
        }
        return letterCounts;
    }

    @Override
    public String toString() {
        return "SolverV4{}";
    }
}
