package com.mollin.lightsoutsolver.core.base;

import com.mollin.lightsoutsolver.core.utils.Coord;

/**
 * Interface représentant une grille de jeu. L'origine (coordonnée (0,0)) se
 * trouve en haut à gauche du tableau. Une coordonée représente le couple
 * (ligne, colonne)
 *
 * @author MOLLIN Florian
 */
public interface GridInterface {
    /**
     * Nombre de lignes de la grille
     *
     * @return Le nombre de ligne de la grille
     */
    public int rows();

    /**
     * Nombre de colonnes de la grille
     *
     * @return Le nombre de colonnes de la grille
     */
    public int columns();

    /**
     * Test si une case est active ou non
     *
     * @param coord La coordonnée de la case à tester
     * @return Vrai si la case est active
     */
    public boolean isActivated(Coord coord);
}
