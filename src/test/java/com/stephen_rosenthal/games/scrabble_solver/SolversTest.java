package com.stephen_rosenthal.games.scrabble_solver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class SolversTest {

    @Test
    public void testSolvers() throws IOException {
        Path dictionaryPath = Paths.get("src/main/resources/sowpods.txt");
        Path letterValuesPath = Paths.get("src/main/resources/letter-values.txt");

        Dictionary dictionary = new Dictionary(dictionaryPath);
        Scorer scorer = new Scorer(letterValuesPath);

        Solver solver1 = new SolverV1(dictionary, scorer);
        Solver solver2 = new SolverV2(dictionary, scorer);
        Solver solver3 = new SolverV3(dictionary, scorer);
        Solver solver4 = new SolverV4(dictionary, scorer);
        List<Solver> solvers = ImmutableList.of(solver1, solver2, solver3, solver4);

        int limit = 20;
        List<String> racks = ImmutableList.of(
                "AARDVARK",
                "ABC",
                "ASDFJKL",
                "BATMAN",
                "BURRITO",
                "FLEAS",
                "GIRAFFE",
                "JABBERWOCKS",
                "ZYZZYX",
                "********"
        );

        // Use SolverV1 as a baseline for a correct answer.
        // It's simple enough that we can be confident about the results.
        Map<String, Collection<ScoredWord>> racksAndResults = racks.stream()
                .collect(Collectors.toMap(rack -> rack, rack -> solver1.getMatches(rack, limit)));

        for (Solver solver : solvers) {
            System.out.println("=====" + solver.toString() + "=====");

            Stopwatch stopwatch = Stopwatch.createUnstarted();
            for (Map.Entry<String, Collection<ScoredWord>> rackAndResult : racksAndResults.entrySet()) {
                String rack = rackAndResult.getKey();
                Collection<ScoredWord> expectedResult = rackAndResult.getValue();

                for (int i = 0; i < 20; i++) {
                    stopwatch.start();
                    Collection<ScoredWord> words = solver.getMatches(rack, limit);
                    stopwatch.stop();

                    assertThat(words).containsExactlyElementsOf(expectedResult);
                }
            }

            System.out.println("Total time = " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
    }

}
