package com.mollin.lightsoutsolver.core.solver;

import com.mollin.lightsoutsolver.core.base.GridInterface;
import com.mollin.lightsoutsolver.core.base.PatternInterface;
import com.mollin.lightsoutsolver.core.solver.solution.Solution;
import com.mollin.lightsoutsolver.core.solver.solution.Solutions;
import com.mollin.lightsoutsolver.core.utils.Coord;
import com.mollin.lightsoutsolver.core.utils.GridUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe permettant de fournir les solutions (ensemble de coordonnées)
 * permettant la résolution d'une grille de jeu suivant un pattern. La méthode
 * de résolution est inspirée du programme de Keith Schwarz ( cf :
 * http://www.keithschwarz.com/interesting/code/?dir=lights-out )
 *
 * @author MOLLIN Florian
 */
public class Solver {
    /**
     * Grille de départ (à résoudre)
     */
    private final GridInterface startGrid;
    /**
     * Grille de fin (objectif)
     */
    private final GridInterface endGrid;
    /**
     * Pattern de la grille
     */
    private final PatternInterface pattern;
    /**
     * Coordonnée spéciale (représente le résultat d'une équation)
     */
    private Coord goalCoord;
    /**
     * Ensemble des equations à résoudre
     */
    private Set<Equation> equations;
    /**
     * Liste des inconnues pouvant prendre les deux valeurs (valeurs non fixées)
     */
    private List<Coord> nonFixedCoords;

    /**
     * Constructeur du solveur.
     *
     * @param startGrid Grille de départ à partir de laquelle trouver la
     *                  solution
     * @param endGrid   Grille à atteindre
     * @param pattern   Pattern utilisé dans la grille
     */
    public Solver(GridInterface startGrid, GridInterface endGrid, PatternInterface pattern) {
        this.startGrid = startGrid;
        this.endGrid = endGrid;
        this.pattern = pattern;
        init();
    }

    /**
     * Constructeur du solveur. La grille à atteindre est la grille par défaut
     * (toutes les cases allumées).
     *
     * @param startGrid Grille de départ à partir de laquelle trouver la
     *                  solution
     * @param pattern   Pattern utilisé dans la grille
     */
    public Solver(GridInterface startGrid, PatternInterface pattern) {
        this(startGrid, GridUtils.getFullGrid(startGrid.rows(), startGrid.columns()), pattern);
    }

    /**
     * Initialise les equations et les ensembles des valeurs des inconnues de la
     * solution. Il y a autant d'équations que de coordonnées sur la grille. Une
     * équation correspond aux cases impactées lors d'un 'clic' sur la
     * coordonnée correspondante; le résultat d'une équation est fonction de la
     * grille de départ et d'arrivée. Il y a plusieurs ensembles de valeurs
     * d'inconnues (les cases devant être cliquées) car il y a potentiellement
     * plusieurs solutions.
     */
    private void init() {
        int rows = this.startGrid.rows();
        int cols = this.startGrid.columns();
        Coord minCoord = Coord.of(0, 0);
        Coord maxCoord = Coord.of(rows - 1, cols - 1);
        // représente le resultat d'une équation (vaut toujours 1)
        this.goalCoord = Coord.of(rows, cols);
        // initialisation des equations
        this.equations = new HashSet<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Coord acCoord = Coord.of(r, c);
                Set<Coord> switchedCoords = this.pattern.getSwitchedCoords(acCoord);
                // filtre des coordonnées impactées par le pattern : on ne
                // conserve que les coordonnées dans la grille
                Set<Coord> filteredSwitchedCoords = switchedCoords.stream()
                        .filter(coord -> coord.isBetween(minCoord, maxCoord))
                        .collect(Collectors.toSet());
                if (this.startGrid.isActivated(acCoord) ^ this.endGrid.isActivated(acCoord)) {
                    filteredSwitchedCoords.add(this.goalCoord);
                }
                this.equations.add(new Equation(filteredSwitchedCoords));
            }
        }
        // initialisation des ensembles de valeurs des inconnues
        this.nonFixedCoords = new ArrayList<>();
    }

    /**
     * Cherche toutes les solutions possibles pour résoudre la grille avec le
     * pattern donné.
     *
     * @return L'ensembles des solutions pour résoudre le système. Chacune des
     * solutions est représentée par un ensemble de coordonnées sur lesquelles
     * il faut 'cliquer'.
     */
    public Solutions solve() {
        return this.solve(Integer.MAX_VALUE);
    }

    /**
     * Cherche les solutions possibles pour résoudre le système. La résolution
     * utilise la méthode du pivot de Gauss pour résoudre les équations et
     * produire un système triangulaire.
     *
     * @param maxSolutions Le nombre maximum de solutions à trouver. (si
     *                     négatif, renvoit toutes les solutions)
     * @return L'ensemble des solutions pour résoudre le système.
     */
    public Solutions solve(int maxSolutions) {
        int rows = this.startGrid.rows();
        int cols = this.startGrid.columns();
        // === etape 1 : triangularisation ===
        Set<Equation> notLockedEquations = new HashSet<>(this.equations);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Coord acCoord = Coord.of(r, c);
                // recherche des equations 'matchantes', celles contenant (r,c)
                // et non vérouillées
                Set<Equation> matchedEquations = notLockedEquations.stream()
                        .filter(e -> e.match(acCoord))
                        .collect(Collectors.toSet());
                if (!matchedEquations.isEmpty()) {
                    // si une équation est trouvée, on la vérouille, et on supprime
                    // (r,c) des autres équations
                    Equation acEquation = matchedEquations.iterator().next();
                    matchedEquations.remove(acEquation);
                    notLockedEquations.remove(acEquation);
                    matchedEquations.forEach(eq -> eq.xor(acEquation));
                } else {
                    // si aucune équation n'est trouvée, (r,c) peut avoir les deux
                    // valeurs (0 et 1), on ajoute donc (r, c) aux inconnues n'ayant
                    // pas de valeur fixée
                    this.nonFixedCoords.add(acCoord);
                }
            }
        }

        // === etape 2 : vérification de la possibilité de résoudre le système ===
        // si une équation ne contient qu'une inconnue qui est la coordonnée
        // spéciale 'goalCoord' (qui vaut 1), alors le système est impossible
        // à résoudre (car on aurait une équation 0 = 1)
        boolean impossible = this.equations.stream()
                .anyMatch(eq -> eq.getUnknowns().size() == 1 && eq.getUnknowns().contains(this.goalCoord));
        if (impossible) {
            return new Solutions(0);
        }

        // === etape 3 : calcul du nombre de solutions totales et du nombre de solutions à calculer ===
        long nbSolutions = (long) Math.pow(2, this.nonFixedCoords.size());
        maxSolutions = (maxSolutions < 0) ? Integer.MAX_VALUE : maxSolutions;
        long nbSolutionsToCompute = Math.min(maxSolutions, nbSolutions);

        // === étape 4 : resolution du système avec les différentes solutions ===
        Set<Map<Coord, Boolean>> computedSolutions = new HashSet<>();
        for (int step = 0; step < nbSolutionsToCompute; step++) {
            Map<Coord, Boolean> solution = new HashMap<>();
            // ajout de l'inconnue représentant le résultat de l'équation
            solution.put(this.goalCoord, true);
            // génération de valeurs fixes pour les inconnues non fixées grâce
            // aux bits de la valeur de l'étape ('step')
            for (int i = 0; i < this.nonFixedCoords.size(); i++) {
                solution.put(this.nonFixedCoords.get(i), (step >> i & 1) == 1);
            }

            // on retire les equations 'vides'
            List<Equation> equationsSet = this.equations.stream()
                    .filter(eq -> !eq.getUnknowns().isEmpty())
                    .sorted((eq1, eq2) -> Integer.compare(eq1.getUnknowns().size(), eq2.getUnknowns().size()))
                    .collect(Collectors.toCollection(LinkedList::new));
            // tant que toutes les equations n'ont pas été traitées
            while (!equationsSet.isEmpty()) {
                Equation eqToRemove = null;
                // recherche de la première équation ne contenant qu'une inconnue
                // indeterminée
                for (Equation eq : equationsSet) {
                    Coord unknownWithoutValue = eq.hasOneUnknownWithoutValue(solution);
                    if (unknownWithoutValue != null) {
                        // si une inconnue n'a pas encore de valeur, on complete
                        // celle-ci dans la solution
                        eq.fillValue(unknownWithoutValue, solution);
                        eqToRemove = eq;
                        break;
                    }
                }
                // on supprime l'equation de l'ensemble à parcourir
                equationsSet.remove(eqToRemove);
            }
            computedSolutions.add(solution);
        }

        // étape 5 : formattage du résultat
        Solutions solutions = new Solutions(nbSolutions);
        computedSolutions.stream()
                .map(uvMap -> {
                    Solution sol = new Solution(uvMap);
                    sol.remove(this.goalCoord);
                    return sol;
                })
                .forEach(solutions::addSolution);
        return solutions;
    }

    /**
     * Cherche la première solution permettant de résoudre le système.
     *
     * @return Une solution permettant de résoudre le système (l'optionnel sera
     * vide si il n'y a pas de solution au système)
     */
    public Optional<Solution> findFirstSolution() {
        Solutions solutions = this.solve(1);
        if (solutions.getComputedSolutions().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(solutions.getComputedSolutions().iterator().next());
        }
    }
}
