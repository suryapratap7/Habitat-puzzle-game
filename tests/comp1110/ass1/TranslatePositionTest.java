package comp1110.ass1;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TranslatePositionTest {
    @Test
    public void testSimpleOriginOnGrid() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int origin = r.nextInt(Habitat.PLACES);
            Piece piece = Piece.values()[r.nextInt(Piece.values().length)];
            Orientation orientation = Orientation.W;
            int t = piece.translatePosition(0,origin,orientation);
            assertTrue("Piece "+('A'+origin)+piece+orientation+", index 0 should be "+origin+" but was "+t,t==origin);
        }
    }

    @Test
    public void testRotateOffGrid() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Orientation orientation = Orientation.values()[r.nextInt(Orientation.values().length)];
            Piece p = Piece.values()[r.nextInt(Piece.values().length)];
            int index = 1;
            int origin = 0;

            switch (orientation) {
                case W: // 0 index 1 will be off grid when on right edge
                    origin = (Habitat.SIDE-1)+Habitat.SIDE*r.nextInt(Habitat.SIDE); break;
                case X: // 90 index 1 will be off grid when on bottom edge
                    origin = Habitat.SIDE*(Habitat.SIDE-1)+r.nextInt(Habitat.SIDE); break;
                case Y: // 180 index 1 will be off grid when on left edge
                    origin = Habitat.SIDE*r.nextInt(Habitat.SIDE); break;
                case Z: // 270 index 1 will be off grid when on top edge
                    origin = r.nextInt(Habitat.SIDE); break;
            }
            int t = p.translatePosition(index,origin,orientation);
            assertTrue("Piece "+(char)('A'+origin)+p+orientation+", index "+index+" should be -1 but was "+t,t==-1);
        }
    }

    @Test
    public void testRotateOnGrid() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Orientation orientation = Orientation.values()[r.nextInt(Orientation.values().length)];
            Piece p = Piece.values()[r.nextInt(Piece.values().length)];
            int index = 1;
            int limit = r.nextInt(Habitat.SIDE-1);
            int full = r.nextInt(Habitat.SIDE);
            int origin = 0;
            int translated = 0;

            switch (orientation) {
                case W: // avoid right edge
                    origin = limit+(full*Habitat.SIDE);
                    translated = origin + 1;
                    break;
                case X: // avoid bottom edge
                    origin = full+(limit*Habitat.SIDE);
                    translated = origin + Habitat.SIDE;
                    break;
                case Y: // avoid left edge
                    origin = 1+limit+(full*Habitat.SIDE);
                    translated = origin - 1;
                    break;
                case Z: // avoid top edge
                    origin = Habitat.SIDE + full+(limit*Habitat.SIDE);
                    translated = origin - Habitat.SIDE;
            }
            int t = p.translatePosition(index,origin,orientation);
            assertTrue("Piece "+(char)('A'+origin)+p+orientation+", index "+index+" should be translated to "+translated+" but instead got "+t,t==translated);
        }
    }


    @Test
    public void testRotateOffGridQT() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Orientation orientation = Orientation.values()[r.nextInt(Orientation.values().length)];
            Piece p = Piece.valueOf(""+(char) ('Q'+r.nextInt(4))); // choose Q..T
            int index = 2;
            int origin = 0;

            switch (orientation) {
                case W: // 90 index 2 will be off grid when on bottom edge
                    origin = Habitat.SIDE*(Habitat.SIDE-1)+r.nextInt(Habitat.SIDE); break;
                case X: // 180 index 2 will be off grid when on left edge
                    origin = Habitat.SIDE*r.nextInt(Habitat.SIDE); break;
                case Y: // 270 index 2 will be off grid when on top edge
                    origin = r.nextInt(Habitat.SIDE); break;
                case Z: // 0 index 2 will be off grid when on right edge
                    origin = (Habitat.SIDE-1)+Habitat.SIDE*r.nextInt(Habitat.SIDE); break;
            }
            int t = p.translatePosition(index,origin,orientation);
            assertTrue("Piece "+(char)('A'+origin)+p+orientation+", index "+index+" should be -1 but was "+t,t==-1);
        }
    }

    @Test
    public void testRotateOnGridQT() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Orientation orientation = Orientation.values()[r.nextInt(Orientation.values().length)];
            Piece p = Piece.valueOf(""+(char) ('Q'+r.nextInt(4))); // choose Q..T
            int index = 2;
            int limit = r.nextInt(Habitat.SIDE-1);
            int full = r.nextInt(Habitat.SIDE);
            int origin = 0;
            int translated = 0;

            switch (orientation) {
                case W: // avoid bottom edge
                    origin = full+(limit*Habitat.SIDE);
                    translated = origin + Habitat.SIDE;
                    break;
                case X: // avoid left edge
                    origin = 1+limit+(full*Habitat.SIDE);
                    translated = origin - 1;
                    break;
                case Y: // avoid top edge
                    origin = Habitat.SIDE + full+(limit*Habitat.SIDE);
                    translated = origin - Habitat.SIDE;
                    break;
                case Z: // avoid right edge
                    origin = limit+(full*Habitat.SIDE);
                    translated = origin + 1;
                    break;
            }
            int t = p.translatePosition(index,origin,orientation);
            assertTrue("Piece "+(char)('A'+origin)+p+orientation+", index "+index+" should be translated to "+translated+" but instead got "+t,t==translated);
        }
    }
}