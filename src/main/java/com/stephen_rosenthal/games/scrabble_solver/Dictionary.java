package com.stephen_rosenthal.games.scrabble_solver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {
    private final List<String> words;

    public Dictionary(Path dictionaryPath) throws IOException {
        this.words = Files.lines(dictionaryPath)
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getWords() {
        return words;
    }
}
