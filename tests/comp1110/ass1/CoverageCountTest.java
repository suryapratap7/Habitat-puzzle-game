package comp1110.ass1;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class CoverageCountTest {

    @Test
    public void testNone() {
        int[] input = new int[Habitat.PLACES];
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        h.setPlacementCoverage(input);
        int coverage = h.getCoverageCount();
        assertTrue("No coverage, so could should be zero, but was: "+coverage, coverage==0);
    }

    @Test
    public void testAll() {
        int[] input = new int[Habitat.PLACES];
        Arrays.fill(input, 1);
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        h.setPlacementCoverage(input);
        int coverage = h.getCoverageCount();
        assertTrue("All covered, so could should be "+Habitat.PLACES+", but was: "+coverage, coverage==Habitat.PLACES);
    }

    @Test
    public void testTwoCovered() {
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int[] input = new int[Habitat.PLACES];
            int idx = r.nextInt(Habitat.PLACES);
            input[idx] = 1 + r.nextInt(2);
            idx += 1 + r.nextInt(Habitat.PLACES - 1);
            input[idx % Habitat.PLACES] = 1+ r.nextInt(2);
            h.setPlacementCoverage(input);
            int errors = h.getCoverageCount();
            assertTrue("Two covered, so could should be two, but was: " + errors, errors == 2);
        }
    }

}