package com.mollin.lightsoutsolver.core.solver;

import com.mollin.lightsoutsolver.core.base.GridInterface;
import com.mollin.lightsoutsolver.core.base.PatternInterface;
import com.mollin.lightsoutsolver.core.solver.solution.Solution;
import com.mollin.lightsoutsolver.core.solver.solution.Solutions;
import com.mollin.lightsoutsolver.core.utils.Coord;
import com.mollin.lightsoutsolver.core.utils.GridUtils;
import com.mollin.lightsoutsolver.core.utils.PatternUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * Classe contenant les exemples
 *
 * @author MOLLIN Florian
 */
public class Examples {
    private static void example1() {
        GridInterface startGrid = GridUtils.getEmptyGrid(4, 4);
        GridInterface finalGrid = GridUtils.getFullGrid(4, 4);
        PatternInterface pattern = PatternUtils.getClassicPattern();

        Solver solver = new Solver(startGrid, finalGrid, pattern);
        // compute all solutions
        Solutions solutions = solver.solve();

        System.out.println(solutions);
    }
    
    private static void example2() {
        GridInterface startGrid = GridUtils.getEmptyGrid(20, 20);
        GridInterface finalGrid = GridUtils.getFullGrid(20, 20);
        PatternInterface pattern = PatternUtils.getClassicPattern();

        Solver solver = new Solver(startGrid, finalGrid, pattern);
        // compute only the first one solution (if exists)
        Optional<Solution> firstSolution = solver.findFirstSolution();
        
        firstSolution.ifPresent(System.out::println);
    }
    
    private static void example3() {
        // custom start grid
        GridInterface startGrid = GridUtils.getGridWithSomeActivatedCoords(3, 3,
                Coord.of(0, 0), Coord.of(1, 0), Coord.of(2, 2)
        );
        // custom final grid
        GridInterface finalGrid = GridUtils.getGridWithSomeActivatedCoords(3, 3,
                Coord.of(0, 1), Coord.of(1, 0), Coord.of(1, 1),
                Coord.of(1, 2), Coord.of(2, 1)
        );
        PatternInterface pattern = PatternUtils.getClassicPattern();

        Solver solver = new Solver(startGrid, finalGrid, pattern);
        Solutions solutions = solver.solve();

        System.out.println(solutions);
    }
    
    private static void example4() {
        GridInterface startGrid = GridUtils.getEmptyGrid(42, 42);
        // custom pattern
        PatternInterface pattern = (coord) -> {
            return new HashSet<>(Arrays.asList(
                    coord.add(Coord.of(1, 1)), coord.add(Coord.of(-1, -1)),
                    coord.add(Coord.of(-1, 1)), coord.add(Coord.of(1, -1)),
                    coord.add(Coord.of(1, 0)), coord.add(Coord.of(0, 1)),
                    coord.add(Coord.of(-1, 0)), coord.add(Coord.of(0, -1))
            ));
        };

        // by default, finalGrid is full
        Solver solver = new Solver(startGrid, pattern);
        Optional<Solution> firstSolution = solver.findFirstSolution();

        firstSolution.ifPresent(System.out::println);
    }
    
    private static void fun1() {
        GridInterface startGrid = GridUtils.getEmptyGrid(250, 250);
        PatternInterface pattern = PatternUtils.getClassicPattern();
        
        Solver solver = new Solver(startGrid, pattern);
        Optional<Solution> firstSolution = solver.findFirstSolution();
        
        firstSolution.ifPresent(System.out::println);
    }
    
    private static void fun2() {
        GridInterface startGrid = GridUtils.getEmptyGrid(250, 250);
        PatternInterface pattern = (coord) -> {
            return new HashSet<>(Arrays.asList(
                    coord.add(Coord.of(1, 1)), coord.add(Coord.of(-1, -1)),
                    coord.add(Coord.of(-1, 1)), coord.add(Coord.of(1, -1)),
                    coord.add(Coord.of(1, 0)), coord.add(Coord.of(0, 1)),
                    coord.add(Coord.of(-1, 0)), coord.add(Coord.of(0, -1))
            ));
        };
        
        Solver solver = new Solver(startGrid, pattern);
        Optional<Solution> firstSolution = solver.findFirstSolution();
        
        firstSolution.ifPresent(System.out::println);
    }

    public static void main(String[] args) {
        example1();
    }
}
