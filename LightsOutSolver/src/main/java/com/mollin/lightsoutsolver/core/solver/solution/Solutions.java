package com.mollin.lightsoutsolver.core.solver.solution;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente l'ensemble des solutions pour résoudre une grille de jeu
 *
 * @author MOLLIN Florian
 */
public class Solutions {
    /**
     * Le nombre total de solutions différentes pour résoudre la grille. (Peut
     * etre différent du nombre de solutions calculées)
     */
    private final long nbSolutions;
    /**
     * Ensemble des solutions calculées (la taille de cet ensemble est inférieur
     * ou égal au nombre de solutions différentes total)
     */
    private final Set<Solution> computedSolutions;

    /**
     * Constructeur d'un ensemble de solutions. L'ensemble de solutions ne
     * contiendra pas de solution calculée (qui seront à ajouter par la suite)
     *
     * @param nbSolutions Le nombre de solutions différentes pour résoudre la
     * grille
     */
    public Solutions(long nbSolutions) {
        this.nbSolutions = nbSolutions;
        this.computedSolutions = new HashSet<>();
    }

    /**
     * Renvoie le nombre total de solutions de la grille. Ce nombre peut etre
     * différent du nombre de solutions calculées. Il s'agit du nombre réél de
     * solutions
     *
     * @return Le nombre total de solutions
     */
    public long getNbSolutions() {
        return this.nbSolutions;
    }

    /**
     * Renvoie l'ensemble des solutions calculées.
     *
     * @return L'ensemble des solutions calculée.
     */
    public Set<Solution> getComputedSolutions() {
        return this.computedSolutions;
    }

    /**
     * Ajoute une solution calculée à l'ensemble des solutions.
     *
     * @param solution La solution à ajouter
     */
    public void addSolution(Solution solution) {
        this.computedSolutions.add(solution);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("===== Solutions =====\n");
        res.append("Nb solutions : ").append(this.nbSolutions).append("\n");
        res.append("Nb computed solutions : ").append(this.computedSolutions.size()).append("\n");
        res.append("Computed solutions :\n");
        int n = 1;
        int solutionsSize = this.computedSolutions.size();
        for (Solution sol : this.computedSolutions) {
            res.append("(").append(n).append("/").append(solutionsSize).append(")\n");
            res.append(sol);
            res.append("\n");
            n++;
        }
        return res.toString();
    }

}
