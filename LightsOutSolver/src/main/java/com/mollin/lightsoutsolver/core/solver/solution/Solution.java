package com.mollin.lightsoutsolver.core.solver.solution;

import com.mollin.lightsoutsolver.core.utils.Coord;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

/**
 * Représente une solution pour une grille de jeu (et un pattern). Il s'agit de
 * la liste des coordonnées sur lesquelles appliquer le pattern pour résoudre la
 * grille
 *
 * @author MOLLIN Florian
 */
public class Solution extends HashSet<Coord> {
    /**
     * Constructeur d'une solution (utiliser principalement par le Solver)
     *
     * @param coordToValue La table faisant la correspondance entre une
     * coordonnée et son état (si l'on doit appliquer ou non le pattern sur
     * celle-ci)
     */
    public Solution(Map<Coord, Boolean> coordToValue) {
        super();
        if (coordToValue != null) {
            coordToValue.entrySet().stream()
                    .filter(entry -> entry.getValue())
                    .map(entry -> entry.getKey())
                    .forEach(this::add);
        }
    }

    /**
     * Constructeur d'une solution.
     *
     * @param coords Ensemble des coordonnées sur lesquelles appliquer le
     * pattern
     */
    public Solution(Coord... coords) {
        super(Arrays.asList(coords));
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        }
        StringBuilder res = new StringBuilder();
        int maxRow = 0;
        int maxCol = 0;
        for (Coord coord : this) {
            maxRow = Integer.max(maxRow, coord.getRow());
            maxCol = Integer.max(maxCol, coord.getColumn());
        }
        for (int r = 0; r <= maxRow; r++) {
            for (int c = 0; c <= maxCol; c++) {
                Coord coord = Coord.of(r, c);
                res.append(this.contains(coord) ? "X " : "_ ");
            }
            if (r != maxRow) {
                res.append("\n");
            }
        }
        return res.toString();
    }
}
