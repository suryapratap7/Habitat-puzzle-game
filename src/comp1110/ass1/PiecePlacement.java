package comp1110.ass1;

/**
 * This class describes a particular placement of a piece.  It is described
 * in terms of three attributes:
 *
 * - the location of the origin of the piece on the habitat grid
 * - the piece that is being placed
 * - the orientation of the piece
 *
 * This information can all be encoded using letters of the alphabet:
 *
 * Locations on the habitat are encoded with letters 'A' to 'P':
 *
 * A B C D
 * E F G H
 * I J K L
 * M N O P
 *
 * Pieces are encoded with letters 'Q' to 'V' (see the Pieces class).
 *
 * Orientations are encoded with letters 'W' to 'Z' (see the Orientation class).
 *
 * So a piece placement "AQW" would describe piece Q, with origin at location 0
 * ('A', top left), and with orientation 0 degrees, covering squares 0, 1 and 4:
 *
 *  X  X  2  3
 *  X  5  6  7
 *  8  9 10 11
 * 12 13 14 15
 *
 * A piece placement "KSZ" would describe piece S, with origin at location 10
 * ('K', center right), with orientation 270 degrees, covering squares 10, 6 and 11:
 *
 *  0  1  2  3
 *  4  5  X  7
 *  8  9  X  X
 * 12 13 14 15

 *
 */
public class PiecePlacement {
    private int origin;
    private Piece piece;
    private Orientation orientation;

    /**
     * Constructor.
     *
     * Take a piece placement string (such as "AQW"), decode the string
     * and create a piece placement object that represents that piece,
     * origin, and orientation.
     *
     * @param pieceplacement A string encoding a piece placement.
     */
    PiecePlacement(String pieceplacement) {
        origin = pieceplacement.charAt(0)-'A';
        piece = Piece.valueOf(pieceplacement.substring(1,2));
        orientation = Orientation.valueOf(pieceplacement.substring(2,3));
        if (origin < 0 || origin > Habitat.PLACES - 1 || piece == null || orientation == null)
            throw new IllegalArgumentException("Bad piece placement string: "+pieceplacement);
    }

    /**
     * Update coverage and error arrays according to this piece placement and a particular
     * habitat.
     *
     * @param habitat  The habitat against which this piece placement has been made
     * @param coverage  An array describing how well each square of the habitat is covered
     * @param errors  An array describing on which squares of the habitat there are errors.
     */
    void updateCoverageAndErrors(Habitat habitat, int[] coverage, boolean[] errors) {
         piece.updateCoverageAndErrors(habitat, origin, orientation, coverage, errors);
    }

    /**
     * Given a string representing a number of piece placements, decode the string and
     * return an array of PiecePlacement objects representing those piece placements.
     *
     * @param placement A string of piece placements (eg "AQW", "FRXJSY", etc).
     * @return An array of PiecePlacements that corresponds to the pieceplacements encoded
     * by the string.
     */
    static PiecePlacement[] getPlacements(String placement) {
        if (!isPlacementWellFormed(placement)) throw new IllegalArgumentException("Bad placement string: "+placement);

        PiecePlacement[] placements = new PiecePlacement[placement.length()/3];
        for (int p = 0; p < placements.length; p++) {
            placements[p] = new PiecePlacement(placement.substring(p*3,(p+1)*3));
        }
        return placements;
    }

    /**
     * Determine whether a piece placement string is well-formed:
     *  - It must be a valid length (a multiple of three and
     *    no more than six x three long)
     *  - It must be free of duplicate piece placements.
     *  - Each of the piece placements within it must be well formed.
     * @param placement A string representing a piece placment.
     * @return True if the string is well formed.
     */
    private static boolean isPlacementWellFormed(String placement) {
        return isPlacementValidLength(placement)
                && isPlacementDuplicateFree(placement)
                && isEachPieceTripleWellFormed(placement);
    }

    /**
     * Determine whether a placement string is a valid length.
     *
     * The length of the string must be:
     *  - a multiple of three in length (each three characters represents one piece), and
     *  - no longer than three times the number of possible pieces (there are six).
     * @param string  A string describing a placement of pieces on the board.
     * @return True if the string is a valid length.
     */
    static boolean isPlacementValidLength(String string) {
        // FIXME Task 2: fix this code so that it determines whether the string is a valid length.

        if (string.length() <= 18 && string.length()%3 == 0){
            return true;
        } else {
            return false;
        }



    }

    /**
     * Determine whether the placement string is free of duplicate pieces.
     * The placement string describes a number of pieces (each piece description is three
     * characters long).   No piece should occur twice in a placement string.
     * @param placement  A string describing the placement of one or more pieces.
     * @return True if the string does not contain duplicates.
     */
    static boolean isPlacementDuplicateFree(String placement) {
        // FIXME Task 6: fix this code so that it correctly determines whether the placement string is free of duplicates
        String[] piece = new String[placement.length()/3];
        int num = 0;
        for (int i = 0; i< placement.length()/3; i++){
            piece[i]= placement.substring(3*i,3*i+3);
            for (int j=0; j<=i; j++){
                if (piece[i].equals(piece[j])){
                    num = num + 1;
                }
            }
        }
        if (num > placement.length()/3){
            return false;
        } else {
            return  true;
        }
    }

    /**
     * Determine whether each of the three-character placements within a
     * placement string are well-formed.
     * @param placement A string describing one or more piece placements
     * @return True of each of the pieces described by the string are well-formed.
     */
    private static boolean isEachPieceTripleWellFormed(String placement) {
        for(int i = 0; i < placement.length(); i += 3) {
            if (!isPieceTripleWellFormed(placement.substring(i,i+3)))
                return false;
        }
        return true;
    }

    /**
     * Determine whether the string is correctly formed.
     *
     * - The string must be exactly three characters long.
     * - It must be all in upper case.
     * - The first character must represent a board position, using a character from 'A' to 'P'.
     * - The second character must represent a piece, using a character from 'Q' to 'V'.
     * - The third character must represent the piece's orientation, using a character from 'W' to 'Z'.
     * @param triple A string that represents a piece.
     * @return True if the string triple has the correct format for a piece placement description.
     */
    static boolean isPieceTripleWellFormed(String triple) {
        // FIXME Task 5: determine whether this string is correctly formed (according to the rules in the comment above).
        boolean bool = false;
        char position;
        char piece;
        char orientation;
        if (triple.length() == 3){
            triple.toUpperCase();
            for (position = 'A'; position <= 'P'; position++){
                if (triple.charAt(0) == position){
                    for (piece = 'Q'; piece <= 'V'; piece++){
                        if (triple.charAt(1) == piece){
                           for (orientation = 'W'; orientation <= 'Z'; orientation++){
                               if (triple.charAt(2) == orientation){
                                   bool = true;
                               }
                           }
                        }
                    }
                }
            }
        }
        return bool;
    }

    /** @return a string that represents this piece placement */
    @Override
    public String toString() {
        return ""+('A' + origin)+('Q' + piece.ordinal())+('W' + orientation.ordinal());
    }
}
