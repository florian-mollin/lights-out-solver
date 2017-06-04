package com.mollin.lightsoutsolver.core.solver;

import com.mollin.lightsoutsolver.core.utils.Coord;
import java.util.Map;
import java.util.Set;

/**
 * Représente une équation avec ses inconnues. L'équation est modulo 2 et égale
 * à 0.
 *
 * @author MOLLIN Florian
 */
public class Equation {
    /**
     * Ensemble des inconnues de l'équation
     */
    private final Set<Coord> unknowns;

    /**
     * Constructeur d'une equation avec la liste de ses inconnues
     *
     * @param unknowns Liste des inconnues de l'équation
     */
    public Equation(Set<Coord> unknowns) {
        this.unknowns = unknowns;
    }

    /**
     * Vérifie si une inconnue (coordonnée) 'matche' avec l'équation. Une
     * inconnue matche si elle est présente dans l'équation
     *
     * @param coord L'inconnue
     * @return Vrai si l'inconnue 'matche'
     */
    public boolean match(Coord coord) {
        return this.unknowns.contains(coord);
    }

    /**
     * Fusionne l'équation avec celle donnée en paramètre. Effectue un ou
     * exclusif (xor) entre les inconnues des équations.
     *
     * @param eq L'équation avec laquelle effectuer la fusion
     */
    public void xor(Equation eq) {
        eq.unknowns.forEach(e -> {
            if(!this.unknowns.remove(e)) {
                this.unknowns.add(e);
            }
        });
    }

    /**
     * Vérifie si l'équation ne possède qu'une inconnue sans valeur (d'après les
     * valeurs des inconnues données en paramètres) et renvoit celle-ci.
     *
     * @param unknownsValues La liste des valeurs des inconnues
     * @return L'unique inconnue n'ayant pas de valeur dans l'équation. Null si
     * aucune ou plus d'une inconnue n'a pas de valeur.
     */
    public Coord hasOneUnknownWithoutValue(Map<Coord, Boolean> unknownsValues) {
        Coord res = null;
        for (Coord c : this.unknowns) {
            if (!unknownsValues.containsKey(c)) {
                if (res == null) {
                    res = c;
                } else {
                    return null;
                }
            }
        }
        return res;
    }

    /**
     * Trouve la valeur de l'inconnue donnée grâce à la table des valeurs des
     * inconnues donnée et remplit celle-ci. Attention : avant d'utiliser cette
     * méthode, vérifier que toutes les inconnues (hormis celle donnée en
     * paramètre) possèdent des valeurs dans la table.
     *
     * @param unknownWithoutValue L'inconnue à trouver
     * @param unknownsValues La table à remplir avec la valeur de l'inconnue trouvée.
     * (Table servant aussi à trouver la valeur de l'inconnue)
     */
    public void fillValue(Coord unknownWithoutValue, Map<Coord, Boolean> unknownsValues) {
        boolean value = this.unknowns.stream()
                .filter((u) -> u != unknownWithoutValue)
                .map((u) -> unknownsValues.get(u))
                .reduce(false, (b1, b2) -> b1 ^ b2);
        unknownsValues.put(unknownWithoutValue, value);
    }

    /**
     * Renvoit l'ensemble des inconnues.
     *
     * @return Ensemble des inconnues
     */
    public Set<Coord> getUnknowns() {
        return unknowns;
    }

    @Override
    public String toString() {
        return "Equation{" + "unknowns=" + unknowns + '}';
    }

}
