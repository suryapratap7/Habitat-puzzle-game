package comp1110.ass1;

import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidLengthTest {
    static final String P = "MQZESZBUWKRYDVXPTY";

    @Test
    public void testEmpty() {
        assertTrue("Empty placement string, should be OK, but was not", PiecePlacement.isPlacementValidLength(""));
    }

    @Test
    public void testComplete() {
        assertTrue("Complete placement string '"+P+"', should be OK, but was not", PiecePlacement.isPlacementValidLength(P));
    }

    @Test
    public void testLong() {
        assertFalse("Long placement string '"+P+"XXX', is long, but was OK", PiecePlacement.isPlacementValidLength(P+"XXX"));
    }

    @Test
    public void testGood() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int p = r.nextInt(Piece.values().length - 2);
            String test = P.substring(0, (p * 3));
            assertTrue("Good placement string '" + test + " is multiple of three, but failed", PiecePlacement.isPlacementValidLength(test));
        }
    }

    @Test
    public void testBad() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int p = r.nextInt(Piece.values().length - 2);
            int x = 1 + r.nextInt(2);
            String test = P.substring(0, (p * 3) + x);
            assertFalse("Bad placement string '" + test + " is not multiple of three, but passed", PiecePlacement.isPlacementValidLength(test));
        }
    }
}