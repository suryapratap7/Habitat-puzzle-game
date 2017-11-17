package comp1110.ass1;

import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class DuplicateFreeTest {
    static final String P = "MQZESZBUWKRYDVXPTY";

    @Test
    public void testEmpty() {
        assertTrue("Empty placement string, should be OK, but was not", PiecePlacement.isPlacementDuplicateFree(""));
    }

    @Test
    public void testComplete() {
        assertTrue("Complete placement string '"+P+"', should be OK, but was not", PiecePlacement.isPlacementDuplicateFree(P));
    }

    @Test
    public void testGood() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int p = r.nextInt(Piece.values().length - 2);
            String test = P.substring(0, (p * 3));
            assertTrue("Good placement string '" + test + " has no duplicates but failed", PiecePlacement.isPlacementDuplicateFree(test));
        }
    }

    @Test
    public void testBad() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int p = 2 + r.nextInt(Piece.values().length - 2);
            String[] pieces = new String[p];
            for (int j = 0; j < p; j++) {
                pieces[j] = P.substring(j*3, (j+1)*3);
            }
            int src = r.nextInt(p);   // duplicate source
            int dst = (src + 1 + r.nextInt(p - 1))%p;  // duplicate destination
            pieces[dst] = pieces[src];
            String test = "";
            for (int j = 0; j < p; j++)
                test += pieces[j];
            assertFalse("Bad placement string '" + test + " contains duplicates, but passed", PiecePlacement.isPlacementDuplicateFree(test));
        }
    }
}