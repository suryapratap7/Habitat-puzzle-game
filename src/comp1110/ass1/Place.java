package comp1110.ass1;

/**
 * A simple enumeration describing the three kinds of place (habitats),
 * land ('L'), water ('W'), and predator ('P').
 */
public enum Place {
    L, W, P;

    /**
     * Determine whether this place is hostile to a particular creature
     *
     * @param creature The creature in question.
     * @return True if this piece is hostile to that creature.
     */
    boolean hostileTo(Creature creature) {
        if (this == P) return true;
        else if (this == L) return creature.equals(Creature.FISH);
        else return creature.equals(Creature.ANIMAL);
    }
}
