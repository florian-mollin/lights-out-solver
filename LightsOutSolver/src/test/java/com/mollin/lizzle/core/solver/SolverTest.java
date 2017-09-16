package com.mollin.lizzle.core.solver;

import com.mollin.lightsoutsolver.core.base.GridInterface;
import com.mollin.lightsoutsolver.core.base.PatternInterface;
import com.mollin.lightsoutsolver.core.solver.Solver;
import com.mollin.lightsoutsolver.core.solver.solution.Solution;
import com.mollin.lightsoutsolver.core.solver.solution.Solutions;
import com.mollin.lightsoutsolver.core.utils.Coord;
import com.mollin.lightsoutsolver.core.utils.GridUtils;
import com.mollin.lightsoutsolver.core.utils.PatternUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test pour le solveur
 *
 * @author MOLLIN Florian
 */
@RunWith(JUnitParamsRunner.class)
public class SolverTest {
    /**
     * Pattern 'vide'
     */
    private static final PatternInterface EMPTY_PATTERN = (coord) -> {
        return new HashSet<>();
    };

    /**
     * Paramètres pour le test de présence de solution vide.
     *
     * @return Les paramètres du test
     */
    private Object[] parametersForEmptySolution() {
        GridInterface nonConstantGrid = GridUtils.getGridWithSomeActivatedCoords(2, 2, Arrays.asList(
                Coord.of(0, 0)
        ));
        return new Object[][]{
                // grille pleine -> pleine avec un pattern classique
                {GridUtils.getFullGrid(5, 5), PatternUtils.getClassicPattern()},
                // grille pleine -> pleine avec un pattern vide
                {GridUtils.getFullGrid(2, 2), EMPTY_PATTERN},
                // grille vide -> vide avec un pattern classique
                {GridUtils.getEmptyGrid(5, 5), PatternUtils.getClassicPattern()},
                // grille vide -> vide avec un pattern vide
                {GridUtils.getEmptyGrid(2, 2), EMPTY_PATTERN},
                // grille irrégulière -> irrégulière avec pattern classique
                {nonConstantGrid, PatternUtils.getClassicPattern()},
                // grille irrégulière -> irrégulière avec pattern vide
                {nonConstantGrid, EMPTY_PATTERN}
        };
    }

    /**
     * Test vérifiant la présence d'une solution vide dans l'ensemble des
     * solutions. Le solveur utilisera la même grille de départ et d'arrivée
     * afin de générer une solution vide.
     *
     * @param grid    Grille de départ et d'arrivée pour le solveur
     * @param pattern Pattern utilisé par le solveur
     */
    @Test
    @Parameters
    public void emptySolution(GridInterface grid, PatternInterface pattern) {
        Solver solver = new Solver(grid, grid, pattern);
        Solutions solutions = solver.solve();
        Optional<Solution> firstSolution = solver.findFirstSolution();
        assertThat(solutions.getNbSolutions())
                .as("Nb solutions")
                .isGreaterThanOrEqualTo(1);
        Solution emptySolution = new Solution();
        assertThat(solutions.getComputedSolutions())
                .as("Computed solutions")
                .isNotEmpty()
                .contains(emptySolution);
        assertThat(firstSolution)
                .as("First solution")
                .isNotEmpty()
                .contains(emptySolution);
    }

    /**
     * Parametres pour le test vérifiant les configurations du solveur ne
     * produisant aucune solution.
     *
     * @return Les parametres du test
     */
    private Object[] parametersForNoSolution() {
        // création d'un pattern 'impossible' pour résoudre une grille vide -> pleine
        PatternInterface impossiblePattern = (coord) -> {
            return new HashSet<>(Arrays.asList(
                    coord.add(Coord.of(-1, 0)),
                    coord.add(Coord.of(-1, 1)),
                    coord.add(Coord.of(0, -1))
            ));
        };
        return new Object[][]{
                // grille vide -> pleine avec un pattern vide
                {GridUtils.getEmptyGrid(3, 3), GridUtils.getFullGrid(3, 3), EMPTY_PATTERN},
                // grille vide -> pleine avec un pattern 'impossible'
                {GridUtils.getEmptyGrid(5, 4), GridUtils.getFullGrid(5, 4), impossiblePattern},
                // grille vide -> grille inatteignable avec un pattern classique
                {GridUtils.getEmptyGrid(5, 5), GridUtils.getGridWithSomeActivatedCoords(5, 5, Coord.of(0, 0)), PatternUtils.getClassicPattern()}
        };
    }

    /**
     * Test vérifiant qu'il n'y a pas de solution lors de certaines
     * configuration du solveur.
     *
     * @param startGrid Grille de départ
     * @param endGrid   Grille d'arrivée
     * @param pattern   Le pattern
     */
    @Test
    @Parameters
    public void noSolution(GridInterface startGrid, GridInterface endGrid, PatternInterface pattern) {
        Solver solver = new Solver(startGrid, endGrid, pattern);
        Solutions solutions = solver.solve();
        Optional<Solution> firstSolution = solver.findFirstSolution();
        assertThat(solutions.getNbSolutions())
                .as("Nb solutions")
                .isEqualTo(0);
        assertThat(solutions.getComputedSolutions())
                .as("Computed solutions")
                .isEmpty();
        assertThat(firstSolution)
                .as("First solution")
                .isEmpty();
    }

    /**
     * Parametres pour le test de solution spécifique.
     *
     * @return Les parametres du test
     */
    private Object[] parametersForSpecificSolution() {
        return new Object[][]{
                {GridUtils.getEmptyGrid(5, 5), PatternUtils.getClassicPattern(), new Solution(Coord.of(0, 0), Coord.of(3, 4))},
                {GridUtils.getEmptyGrid(3, 3), PatternUtils.getClassicPattern(), new Solution(Coord.of(0, 0), Coord.of(1, 1), Coord.of(2, 2))}
        };
    }

    /**
     * Test vérifiant la présence d'une solution spécifique. La grille d'arrivée
     * est automatiquement générée grâce à la solution attendue (et le pattern
     * donné) afin de tester que le solveur trouve cette solution lors de la
     * résolution
     *
     * @param startGrid      Grille de départ
     * @param pattern        Le pattern
     * @param solutionToFind La solution attendue
     */
    @Test
    @Parameters
    public void specificSolution(GridInterface startGrid, PatternInterface pattern, Solution solutionToFind) {
        Set<Coord> activatedCoordsOfStartGrid = getActivatedCoords(startGrid);
        Set<Coord> switchedCoordsOfSolutionWithPattern = getSwitchedCoordsOfSolutionWithPattern(solutionToFind, pattern);
        Set<Coord> activatedCoordsOfEndGrid = xorSets(activatedCoordsOfStartGrid, switchedCoordsOfSolutionWithPattern);
        GridInterface endGrid = GridUtils.getGridWithSomeActivatedCoords(startGrid.rows(), startGrid.columns(), activatedCoordsOfEndGrid);
        Solver solver = new Solver(startGrid, endGrid, pattern);
        Solutions solutions = solver.solve();
        Optional<Solution> firstSolution = solver.findFirstSolution();
        assertThat(solutions.getNbSolutions())
                .as("Nb solutions")
                .isGreaterThanOrEqualTo(1);
        assertThat(solutions.getComputedSolutions())
                .as("Computed solutions")
                .contains(solutionToFind);
        assertThat(firstSolution)
                .as("First solution")
                .isNotEmpty();
    }

    /**
     * Retourne l'ensemble des coordonnées actives d'une grille
     *
     * @param grid La grille
     * @return Les coordonnées active de la grille
     */
    public static Set<Coord> getActivatedCoords(GridInterface grid) {
        Set<Coord> activatedCoords = new HashSet<>();
        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.columns(); c++) {
                Coord coord = Coord.of(r, c);
                if (grid.isActivated(coord)) {
                    activatedCoords.add(coord);
                }
            }
        }
        return activatedCoords;
    }

    /**
     * Renvoi les coordonnées à switcher lors de l'application du pattern sur
     * les coordonnées de la solution. Attention : retourne toute les
     * coordonnées, même celles hors grille (il faudra filtrer)
     *
     * @param solution La solution contenant les coordonnées sur lesquelles
     *                 appliquer le pattern
     * @param pattern  Le pattern à appliquer
     * @return Les coordonnées à switcher suite à l'application du pattern sur
     * la solution
     */
    private Set<Coord> getSwitchedCoordsOfSolutionWithPattern(Solution solution, PatternInterface pattern) {
        return solution.stream()
                .map(coord -> pattern.getSwitchedCoords(coord))
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() % 2 == 1)
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

    /**
     * Effectue un XOR (ou exclusif) entre deux ensembles
     *
     * @param set1 Le premier ensemble
     * @param set2 Le second ensemble
     * @return Ensemble 1 XOR Ensemble 2
     */
    private Set<Coord> xorSets(Set<Coord> set1, Set<Coord> set2) {
        return Stream.of(set1, set2)
                .flatMap(Set::stream)
                .filter(coord -> !set1.contains(coord) || !set2.contains(coord))
                .collect(Collectors.toSet());
    }

    /**
     * Parametres pour le test vérifiant toutes les solutions proposées par le
     * solveur.
     *
     * @return Les paramètres du test
     */
    private Object[] parametersForAllSolutions() {
        List<Object[]> parameters = new ArrayList<>();
        int limitRow = 10;
        int limitColumn = 10;
        for (int r = 0; r <= limitRow; r++) {
            for (int c = 0; c <= limitColumn; c++) {
                parameters.add(new Object[]{
                        GridUtils.getEmptyGrid(r, c),
                        GridUtils.getFullGrid(r, c),
                        PatternUtils.getClassicPattern()
                });
            }
        }
        return parameters.toArray();
    }

    /**
     * Test vérifiant si toutes les solutions proposées par le solveur sont
     * correctes.
     *
     * @param startGrid Grille de départ
     * @param endGrid   Grille de fin
     * @param pattern   Le pattern
     */
    @Test
    @Parameters
    public void allSolutions(GridInterface startGrid, GridInterface endGrid, PatternInterface pattern) {
        Solver solver = new Solver(startGrid, endGrid, pattern);
        Solutions solutions = solver.solve();
        Optional<Solution> firstSolution = solver.findFirstSolution();
        Coord minCoord = Coord.of(0, 0);
        Coord maxCoord = Coord.of(startGrid.rows() - 1, startGrid.columns() - 1);
        assertThat(firstSolution.isPresent())
                .isEqualTo(!solutions.getComputedSolutions().isEmpty());
        for (Solution solution : solutions.getComputedSolutions()) {
            Set<Coord> activatedCoordsWithSolution = xorSets(
                    getActivatedCoords(startGrid),
                    getSwitchedCoordsOfSolutionWithPattern(solution, pattern)
            ).stream().filter(coord -> coord.isBetween(minCoord, maxCoord))
                    .collect(Collectors.toSet());
            assertThat(activatedCoordsWithSolution)
                    .as("Activated coords with solution")
                    .isEqualTo(getActivatedCoords(endGrid));
        }
    }

    /**
     * Parametres pour le test avec limitation du nombre de solutions.
     *
     * @return Les paramètres du test
     */
    private Object[] parametersForLimitSolutions() {
        return new Object[][]{
                {4, 4, 0, 16, 0},
                {4, 4, 1, 16, 1},
                {4, 4, 16, 16, 16},
                {4, 4, 42, 16, 16},
                {23, 47, 3, 4194304, 3}
        };
    }

    /**
     * Test du solveur lorsque l'on impose un nombre limité de solution à
     * calculer. Ce test s'effectuera uniquement d'une grille vide vers une
     * grille pleine
     *
     * @param rows                        Le nombre de lignes des grilles
     * @param columns                     Le nombre de colonnes des grilles
     * @param limitNbSolution             Le nombre limite de solutions à calculer
     * @param expectedNbTotalSolutions    Le nombre total de solutions attendues de
     *                                    la grille
     * @param expectedNbComputedSolutions Le nombre de solutions calculées
     *                                    attendues
     */
    @Test
    @Parameters
    public void limitSolutions(int rows, int columns, int limitNbSolution, int expectedNbTotalSolutions, int expectedNbComputedSolutions) {
        GridInterface startGrid = GridUtils.getEmptyGrid(rows, columns);
        GridInterface endGrid = GridUtils.getFullGrid(rows, columns);
        PatternInterface pattern = PatternUtils.getClassicPattern();
        Solver solver = new Solver(startGrid, endGrid, pattern);
        Solutions solutions = solver.solve(limitNbSolution);
        assertThat(solutions.getNbSolutions()).isEqualTo(expectedNbTotalSolutions);
        assertThat(solutions.getComputedSolutions()).hasSize(expectedNbComputedSolutions);
    }

}
