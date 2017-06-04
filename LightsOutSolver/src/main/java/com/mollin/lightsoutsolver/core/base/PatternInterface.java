package com.mollin.lightsoutsolver.core.base;

import com.mollin.lightsoutsolver.core.utils.Coord;
import java.util.Set;

/**
 * Interface représentant un pattern (motif) de jeu. Une coordonnée représente
 * le couple (ligne, colonne)
 *
 * @author MOLLIN Florian
 */
public interface PatternInterface {

    /**
     * Renvoi l'ensemble des coordonées des cases à 'switcher' lors de
     * l'application du pattern sur la case de coordonnée donnée
     *
     * @param coord La coordonnée sur laquelle appliquer le pattern
     * @return Les coordonnées des cases à 'switcher' lors de l'application du
     * pattern sur la case de coordonée 'coord'
     */
    public Set<Coord> getSwitchedCoords(Coord coord);
}
