package com.mollin.lightsoutsolver.core.utils;

import com.mollin.lightsoutsolver.core.base.GridInterface;

import java.util.Arrays;
import java.util.Collection;

/**
 * Classe utilitaire pour les grilles
 *
 * @author MOLLIN Florian
 */
public class GridUtils {
    /**
     * Constructeur privé car classe utilitaire
     */
    private GridUtils() {
    }

    /**
     * Retourne une grille remplie uniformement
     *
     * @param rows    Le nombre de lignes de la grille
     * @param columns Le nombre de colonne de la grille
     * @param active  Si les cases de la grille doivent être actives ou non
     * @return La grille uniformément remplie
     */
    private static GridInterface getGrid(int rows, int columns, boolean active) {
        return new GridInterface() {
            @Override
            public int rows() {
                return rows;
            }

            @Override
            public int columns() {
                return columns;
            }

            @Override
            public boolean isActivated(Coord coord) {
                return active;
            }
        };
    }

    /**
     * Retourne une grille vide
     *
     * @param rows    Le nombre de lignes de la grille
     * @param columns Le nombre de colonne de la grille
     * @return La grille vide
     */
    public static GridInterface getEmptyGrid(int rows, int columns) {
        return getGrid(rows, columns, false);
    }

    /**
     * Retourne une grille pleine
     *
     * @param rows    Le nombre de lignes de la grille
     * @param columns Le nombre de colonne de la grille
     * @return La grille pleine
     */
    public static GridInterface getFullGrid(int rows, int columns) {
        return getGrid(rows, columns, true);
    }

    /**
     * Retourne une grille de taille définie avec uniquement les cases actives
     * aux coordonnées données.
     *
     * @param rows    Le nombre le lignes de la grille
     * @param columns Le nombre de colonnes de la grille
     * @param coords  La liste des coordonnées des cases à activer dans la grille
     * @return La grille avec certaines cases actives
     */
    public static GridInterface getGridWithSomeActivatedCoords(int rows, int columns, Coord... coords) {
        return getGridWithSomeActivatedCoords(rows, columns, Arrays.asList(coords));
    }

    /**
     * Retourne une grille de taille définie avec uniquement les cases actives
     * aux coordonnées données.
     *
     * @param rows    Le nombre le lignes de la grille
     * @param columns Le nombre de colonnes de la grille
     * @param coords  La liste des coordonnées des cases à activer dans la grille
     * @return La grille avec certaines cases actives
     */
    public static GridInterface getGridWithSomeActivatedCoords(int rows, int columns, Collection<Coord> coords) {
        return new GridInterface() {
            @Override
            public int rows() {
                return rows;
            }

            @Override
            public int columns() {
                return columns;
            }

            @Override
            public boolean isActivated(Coord coord) {
                return coords.contains(coord);
            }
        };
    }
}
