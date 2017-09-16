package com.mollin.lightsoutsolver.core.utils;

import com.mollin.lightsoutsolver.core.base.PatternInterface;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Classe utilitaire pour les patterns
 *
 * @author MOLLIN Florian
 */
public class PatternUtils {
    /**
     * Constructeur privé car classe utilitaire
     */
    private PatternUtils() {
    }

    /**
     * Pattern 'classique'
     */
    private static final PatternInterface CLASSIC_PATTERN = coord -> new HashSet<>(Arrays.asList(
            coord.add(Coord.of(0, 0)),
            coord.add(Coord.of(1, 0)),
            coord.add(Coord.of(0, 1)),
            coord.add(Coord.of(-1, 0)),
            coord.add(Coord.of(0, -1))
    ));

    /**
     * Retourne le pattern 'classique'
     *
     * @return Le pattern classique
     */
    public static PatternInterface getClassicPattern() {
        return CLASSIC_PATTERN;
    }
}
