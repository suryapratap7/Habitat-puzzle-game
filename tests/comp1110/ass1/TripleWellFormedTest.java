package comp1110.ass1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TripleWellFormedTest {
    static final String P = "MQZESZBUWKRYDVXPTY";

    @Test
    public void testSimple() {
        for (int i = 0; i < P.length(); i+= 3) {
            String test = P.substring(i, i+3);
            assertTrue("Simple placement string '"+test+"', should be OK, but was not", PiecePlacement.isPieceTripleWellFormed(test));
        }
    }

    @Test
    public void testUpperCase() {
        for (int i = 0; i < P.length(); i+= 3) {
            String test = P.substring(i, i+3).toLowerCase();
            assertFalse("Simple placement string '"+test+"', is lower case, but passed", PiecePlacement.isPieceTripleWellFormed(test));
        }
    }

    @Test
    public void testGood() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            char a = (char) ('A' + r.nextInt(Habitat.PLACES));
            char b = (char) ('Q' + r.nextInt(Piece.values().length));
            char c = (char) ('W' + r.nextInt(Orientation.values().length));
            String test = ""+a+b+c;
            assertTrue("Well-formed placement string '" + test + " failed", PiecePlacement.isPieceTripleWellFormed(test));
        }
    }

    @Test
    public void testBad() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            char a = (char) ('A' + r.nextInt(Habitat.PLACES));
            char bada = (char) ('Q' + r.nextInt(Piece.values().length+Orientation.values().length));
            char b = (char) ('Q' + r.nextInt(Piece.values().length));
            char badb = (char) ('A' + r.nextInt(Habitat.PLACES));
            char c = (char) ('W' + r.nextInt(Orientation.values().length));
            char badc = (char) ('A' + r.nextInt(Habitat.PLACES+Piece.values().length));
            String test = "";
            switch (r.nextInt(4)) {
                case 0: test += bada+b+c; break;
                case 1: test += a+badb+c; break;
                case 2: test += a+b+badc; break;
                default: test += bada+b+badc;
            }
            assertFalse("Badly-formed placement string '" + test + " passed", PiecePlacement.isPieceTripleWellFormed(test));
        }
    }
}