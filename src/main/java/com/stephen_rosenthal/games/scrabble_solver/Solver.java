package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Collection;

public interface Solver {
    char WILDCARD_CHAR = '*';

    Collection<ScoredWord> getMatches(String rack, int limit);
}
