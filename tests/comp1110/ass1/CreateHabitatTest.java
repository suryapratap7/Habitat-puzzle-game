package comp1110.ass1;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CreateHabitatTest {

    @Test
    public void testCreateHabitat() {
        final int iterations = 5;
        Set<String> habitats = new HashSet();
        Random r = new Random();
        for (int i = 0; i < iterations; i++) {
            Habitat h = new Habitat(10*r.nextDouble());
            String[] sol = h.getSolutions();
            habitats.add(h.toString());
            assertTrue("Habitat " + h + " has "+sol.length+" solutions but must have just one.", sol.length == 1);
            assertTrue("Habitat " + h + " is not a legal habitat.", h.isHabitatLegal());
        }
        assertTrue("The set of habitats generated was only "+habitats.size()+" from " + iterations + " attempts.", habitats.size() >= iterations/2);
    }
}