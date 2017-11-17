package comp1110.ass1;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class ErrorCountTest {

    @Test
    public void testNone() {
        boolean[] input = new boolean[Habitat.PLACES];
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        h.setPlacementErrors(input);
        int errors = h.getErrorCount();
        assertTrue("No errors, so could should be zero, but was: "+errors, errors==0);
    }

    @Test
    public void testAll() {
        boolean[] input = new boolean[Habitat.PLACES];
        Arrays.fill(input, Boolean.TRUE);
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        h.setPlacementErrors(input);
        int errors = h.getErrorCount();
        assertTrue("All errors, so could should be "+Habitat.PLACES+", but was: "+errors, errors==Habitat.PLACES);
    }

    @Test
    public void testTwoErrors() {
        Habitat h = new Habitat("LPLLPWPWWPWPLWWL");
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            boolean[] input = new boolean[Habitat.PLACES];
            int idx = r.nextInt(Habitat.PLACES);
            input[idx] = true;
            idx += 1 + r.nextInt(Habitat.PLACES - 1);
            input[idx % Habitat.PLACES] = true;
            h.setPlacementErrors(input);
            int errors = h.getErrorCount();
            assertTrue("Two errors, so could should be two, but was: " + errors, errors == 2);
        }
    }

}