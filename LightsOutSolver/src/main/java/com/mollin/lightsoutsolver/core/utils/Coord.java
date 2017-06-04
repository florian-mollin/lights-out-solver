package com.mollin.lightsoutsolver.core.utils;

/**
 * Représente une coordonnée. Une coordonnée représente un couple (ligne,
 * colonne)
 *
 * @author MOLLIN Florian
 */
public class Coord {
    /**
     * Numéro de la ligne de la coordonnée
     */
    private final int row;
    /**
     * Numéro de la colonne de la coordonnée
     */
    private final int column;

    /**
     * Constructeur d'une coordonnée avec ligne et colonne.
     *
     * @param row La ligne de la coordonnée
     * @param column La colonne de la coordonnée
     */
    public Coord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Constructeur d'une coordonnée avec ligne et colonne.
     *
     * @param row La ligne de la coordonnée
     * @param column La colonne de la coordonnée
     * @return La coordonnée (ligne, colonne)
     */
    public static Coord of(int row, int column) {
        return new Coord(row, column);
    }

    /**
     * Retourne la ligne de la coordonnée
     *
     * @return La ligne de la coordonnée
     */
    public int getRow() {
        return row;
    }

    /**
     * Retourne la colonne de la coordonnée
     *
     * @return La colonne de la coordonnée
     */
    public int getColumn() {
        return column;
    }

    /**
     * Vérifie si la coordonée est entre les deux autres (le test est inclusif)
     *
     * @param a La première coordonnée
     * @param b La seconde coordonnée
     * @return Vrai si la coordonnée est entre 'a' et 'b'
     */
    public boolean isBetween(Coord a, Coord b) {
        return a.row <= this.row && this.row <= b.row
                && a.column <= this.column && this.column <= b.column;
    }

    /**
     * Renvoi l'addition de la coordonnée courante avec celle donnée
     *
     * @param coord La coordonnée à additionner
     * @return La coordonnée courante + la coordonnée donnée
     */
    public Coord add(Coord coord) {
        return Coord.of(this.row + coord.row, this.column + coord.column);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.row;
        hash = 79 * hash + this.column;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coord other = (Coord) obj;
        if (this.row != other.row) {
            return false;
        }
        return this.column == other.column;
    }

    @Override
    public String toString() {
        return "(" + this.row + ";" + this.column + ")";
    }

}
