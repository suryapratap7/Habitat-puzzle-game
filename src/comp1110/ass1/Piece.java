package comp1110.ass1;

import static comp1110.ass1.Creature.*;

/**
 * An enumeration that describes each of the six transparent pieces that
 * make the game.   The squares that make up each piece are numbered
 * 0, 1, and 2 (the 'L'-shaped pieces have 3 squares, the 'I'-shaped
 * pieces are made up of 2 squares).
 *
 * The 'L'-shaped pieces are described in terms of being
 * in the inverted 'L' position, with indices as follows:
 *
 *  01
 *  2
 *
 * The 'I'-shaped pieces are described in terms of being
 * in the sideways 'I' position, with indices as follows:
 *
 * 01
 *
 * Pieces are named Q, R, S, T, U, & V, with the first
 * four being 'L'-shaped and U & V being 'I'-shaped.
 *
 * All pieces are described in terms of a 2x2 grid:
 *    0 1
 *    2 3
 *
 * The I-shaped pieces (U and V) only occupy the top row of the
 * grid:
 *    X X
 *    2 3
 *
 * The L-shaped pieces occupy all but the bottom right
 * corner of the 2x2 grid:
 *    X X
 *    X 3
 *
 * The U piece is symmetric, so may only appear in the W and X
 * rotations.
 *
 * The squares within the piece are indexed.   Index 0
 * refers to the top left square within the grid, 1 refers
 * to the top right, and 2 refers to the bottom left (there
 * is no index three since the bottom right square is never
 * occupied).
 *
 * The square indexed zero is the 'origin', and is always
 * used to describe the placement of the piece, along
 * with an orientation.  The position is described as
 * a number from 0-15, which indicates where on the habitat
 * the origin is placed (with 0 at top left, and 15 at
 * bottom right):
 *
 *  0  1  2  3
 *  4  5  6  7
 *  8  9 10 11
 * 12 13 14 15
 *
 * For example, piece Q, if located with its origin at 5
 * and with orientation W (unrotated), would cover squares
 * 5, 6, and 9 (a, b, and c represent squares at offset
 * 0, 1, and 2 respectively).
 *
 *  0  1  2  3
 *  4  a  b  7
 *  8  c 10 11
 * 12 13 14 15
 *
 *  If the Q piece were at origin 5 and oriented 'X' (90
 *  degrees clockwise), it would cover squares 5, 9, 4:
 *
 *  0  1  2  3
 *  c  a  6  7
 *  8  b 10 11
 * 12 13 14 15
 */
public enum Piece {
    Q(null, FISH, ANIMAL),
    R(null, ANIMAL, FISH),
    S(FISH, null, ANIMAL),
    T(ANIMAL, null, null),
    U(FISH,FISH),
    V(FISH,ANIMAL);

    /* An array representing the creatures that live on this piece.  Null means the
       square has no creature on it.  Indices are according to
     */
    final Creature[] creatures;


    /**
     * Constructor for an 'L'-shaped piece, which can have three
     * creatures.
     * @param a Creature at index 0
     * @param b Creature at index 1
     * @param c Creature at index 2
     */
    Piece(Creature a, Creature b, Creature c) {
        creatures = new Creature[3];
        creatures[0] = a;
        creatures[1] = b;
        creatures[2] = c;
    }


    /**
     * Constructor for an 'I'-shaped piece, which can have two
     * creatures.
     * @param a Creature at index 0
     * @param b Creature at index 1
     */
    Piece(Creature a, Creature b) {
        creatures = new Creature[2];
        creatures[0] = a;
        creatures[1] = b;
    }


    /**
     * If a piece is moved, we need to determined whether any of the placements are in error
     * and also determine which squares of the habitat are covered.   This method updates
     * the contents of the coverage and errors arrays which are passed as arguments, given
     * a particular habitat (on which the piece is placed), the origin of the piece (describing
     * where the zero-indexed square of the piece is located), and the orientation of the piece.
     *
     * @param habitat The habitat on which this piece was placed
     * @param origin The square on which the origin square is placed
     * @param orientation The orientation of the piece
     * @param coverage The array of coverage, which counts how many pieces are on a given square
     *                 of the habitat (updated by this method)
     * @param errors The array of errors, which has a boolean for each square of the habitat
     *               indicating whether or not there's an error at that square (updated by this
     *               method).
     */
    void updateCoverageAndErrors(Habitat habitat, int origin, Orientation orientation, int[] coverage, boolean[] errors) {
        int[] position = getTranslatedTilePositions(origin, orientation);
        for (int i = 0; i < creatures.length; i++) {
            if (position[i] != -1) {
                if ((creatures[i] != null && habitat.isHostile(position[i], creatures[i]))) {
                    errors[position[i]] = true;
                }
                coverage[position[i]]++;
            }
        }
    }


    /**
     * Return an array that indicates which square of the habitat each square
     * of the piece lies on given an origin and a particular orientation.
     *
     * For example, if piece Q with origin 5 had orientation 'Z' (270), it
     * would lie on squares 5, 1 and 6 (a, b, and c represent squares at offset
     * 0, 1, and 2 respectively):
     *
     *  0  b  2  3
     *  4  a  c  7
     *  8  9 10 11
     * 12 13 14 15
     *
     * so this method would return an array [5, 1, 6]
     *
     * @param origin The square at which the origin of the piece is placed
     * @param orientation  The orientation of the piece
     * @return An array of integers indicating the habitat squares on which each of
     * the squares of the piece lie.
     */
    int[] getTranslatedTilePositions(int origin, Orientation orientation) {
        int[] rtn = new int[creatures.length];
        for (int i = 0; i < creatures.length; i++) {
            rtn[i] = translatePosition(i, origin, orientation);
        }
        return rtn;
    }

    /**
     * The placement of a particular square within a piece is described
     * in terms of:
     *  - the piece it is on,
     *  - where it i on the piece (its index),
     *  - the origin of the piece (the place were index 0 is),
     *  - and the orientation of the piece.
     *
     *
     * The location of a piece is always in terms of where the origin (index 0)
     * is located on the 4x4 habitat.
     *
     * This method must return the location of the square referred to by index.
     * In the case where the index is zero, it simply returns origin (since the
     * origin is unaffected by rotation).  Otherwise, it must calculate the
     * location and return that.
     *
     * For example, if the piece were Q, index were 2, the origin were 5,
     * and the rotation were Z, the method would return 6, since the square
     * at index 2 ('c' in the diagram below) falls on habitat square 6.
     *
     *  0  b  2  3
     *  4  a  c  7
     *  8  9 10 11
     * 12 13 14 15
     *
     * If the indexed square is off the grid, -1 should be returned as the location.
     *
     * @param index  The square within the piece in question.  0 refers to the
     *               origin (top left) 1 to the top right, and 2 to the bottom left.
     * @param origin The location of the origin of the piece within the habitat.  0
     *               refers to the top left square of the 4x4 grid, 3 to the top right,
     *               12 to the bottom left and 15 to the bottom right.
     * @param orientation The orientation of the square.  Orientation is encoded as
     *                    'W' meaning upright,
     *                    'X' meaning rotated 90 degrees clockwise,
     *                    'Y' meaning rotated 180 degrees, and
     *                    'Z' meaning rotated 270 degrees clockwise.
     * @return The location on the grid (a number between 0 and 15) of the indexed
     * square, given the origin of the piece and its orientation, or -1 if the indexed
     * square is off the grid.
     */
    int translatePosition(int index, int origin, Orientation orientation) {
        // FIXME Task 7: correctly translate the position of the square referred to by the index
        int location = 0;
        switch (orientation){
            case W:
                if (index == 2){
                    if (origin == 12 || origin == 13 || origin == 14 || origin == 15){
                        return -1;
                    } else {
                        location = origin + 4;
                    }
                } else  if (index == 1){
                    if (origin == 3 || origin == 7 || origin == 11 || origin == 15){
                        return -1;
                    } else {
                        location = origin + 1;
                    }
                } else if (index==0){
                    location = origin;
                }
                return location;
            case X:
                if (index == 2){
                    if (origin == 4 || origin == 0 || origin == 8 || origin == 12){
                        return -1;
                    } else {
                        location = origin - 1;
                    }
                } else  if (index == 1){
                    if (origin == 12 || origin == 13 || origin == 14 || origin == 15){
                        return -1;
                    } else {
                        location = origin + 4;
                    }
                } else if (index==0){
                    location = origin;
                }
                return location;
            case Y:
                if (index == 2){
                    if (origin == 0 || origin == 1 || origin == 2 || origin == 3){
                        return -1;
                    } else {
                        location = origin - 4;
                    }
                } else  if (index == 1){
                    if (origin == 0 || origin == 4 || origin == 8 || origin == 12){
                        return -1;
                    } else {
                        location = origin - 1;
                    }
                } else if (index==0){
                    location = origin;
                }
                return location;
            case Z:
                if (index == 2){
                    if (origin == 3 || origin == 7 || origin == 11 || origin == 15){
                        return -1;
                    } else {
                        location = origin + 1;
                    }
                } else  if (index == 1){
                    if (origin == 0 || origin == 1 || origin == 2 || origin == 3){
                        return -1;
                    } else {
                        location = origin - 4;
                    }
                } else if (index==0){
                    location = origin;
                }
                return location;
            default:
                return -1;

        }
    }
}
