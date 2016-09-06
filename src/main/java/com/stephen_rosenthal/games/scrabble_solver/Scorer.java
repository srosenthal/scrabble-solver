package com.stephen_rosenthal.games.scrabble_solver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class Scorer {
    private Map<Character, Integer> letterValues;

    public Scorer(Path letterValuesPath) throws IOException {
        this.letterValues = Files.lines(letterValuesPath)
                .collect(Collectors.toMap(
                        line -> line.split(" ")[0].toUpperCase().charAt(0),
                        line -> Integer.parseInt(line.split(" ")[1])));
    }

    public int computeValue(String word) {
        int wordValue = 0;
        for (char letter : word.toCharArray()) {
            wordValue += letterValues.get(letter);
        }
        return wordValue;
    }
}
