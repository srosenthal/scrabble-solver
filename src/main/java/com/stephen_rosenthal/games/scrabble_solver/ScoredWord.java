package com.stephen_rosenthal.games.scrabble_solver;

import java.util.Objects;

public class ScoredWord {
    private final String word;
    private final int pointValue;

    public ScoredWord(String word, int pointValue) {
        this.word = word;
        this.pointValue = pointValue;
    }

    public String getWord() {
        return word;
    }

    public int getPointValue() {
        return pointValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScoredWord that = (ScoredWord) o;
        return pointValue == that.pointValue &&
                Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, pointValue);
    }

    @Override
    public String toString() {
        return "ScoredWord{" +
                "word='" + word + '\'' +
                ", value=" + pointValue +
                '}';
    }
}
