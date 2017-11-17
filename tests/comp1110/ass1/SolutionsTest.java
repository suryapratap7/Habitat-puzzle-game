package comp1110.ass1;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class SolutionsTest {
    static final String[] hs = {"LLWWPWPWWPWPLLPL",   "LWWLPWPWWPWPLLPL",   "WLLWWPWPPWPWLPLL", "LWLWLWLWLWLWLWWP", "PWLWLWLWLWLWLWWP", "LWWWLLLLWWWWWWLW"};
    static final String[] ps = {"JQWCSXERZPTYIVXDUX", "JQWHSYBUWIVXERZPTY", "IQWFRZDSXOTYAUXLVX", "IQWHRYLSXNUWETZBVW", "IQWHRYLSXNUWETZBVW", "CQWMUWLVZOTZBSXIRZ"};

    static final String[] hd = {"LWLLLWLPWLWPLWWL","WWWLLWWPWLWWLWWL","LWWLLWPWWLWWLWWL","LLPWPWLWLWWLWWWL","LPLWWWLWLLPLWWWL","LWPWPWLWLLLLWWWL"};
    static final String[][] pd = {{"AQWLRXISWNUWFVWDTX","AQWLRXFSWDTXNUWIVX"},{"FQWDTXNUWLRXASWIVX","AQWLRXFSWNUWIVXDTX"},{"KQXLVXERZBUWMTZHSY","AQWLRXFSWDTXNUWIVX"},
            {"CQWFUXOSZATWHVXMRZ","CQWMUWJRYOSZHVXATW"},{"CQWMUWHVXATWOSZJRY","DQXEVZLTXFSZIRWNUW"},{"CQWMUWBSXLRYOVWITZ","CQWMUWOSZATWHVXJRY"}};

    @Test
    public void testSingleSolution() {
        for (int i = 0; i < hs.length; i++) {
            Habitat h = new Habitat(hs[i]);
            String[] sol = h.getSolutions();
            String ss = "";
            for(String s : sol) ss += " "+s;
            assertTrue("Habitat " + hs[i] + " has one solution, " + ps[i] + ", but got: " + ss, sol.length == 1);
            assertTrue("Habitat " + hs[i] + " has solution: " + ps[i] + ", but got: " + ss, isomorphic(sol[0],(ps[i])));
        }
    }

    @Test
    public void testDoubleSolution() {
        for (int i = 0; i < hs.length; i++) {
            Habitat h = new Habitat(hd[i]);
            String[] sol = h.getSolutions();
            String ss = "";
            for(String s : sol) ss += " "+s;
            assertTrue("Habitat " + hd[i] + " has two solutions: " + pd[i][0] + " and " + pd[i][1] + ", but got: " + ss, sol.length == 2);
            assertTrue("Habitat " + hd[i] + " has two solutions: " + pd[i][0] + " and " + pd[i][1] + ", but got: " + ss, isomorphic(sol,(pd[i])));
        }
    }

    private boolean isomorphic(String[] p1, String[] p2) {
        Set<String> s1 = new HashSet<>(Arrays.asList(p1));
        Set<String> s2 = new HashSet<>(Arrays.asList(p2));
        Set<String> match = new HashSet<>();
        for(String s1a : s1) {
            for(String s2a : s2) {
                if (isomorphic(s1a, s2a)) {
                    match.add(s1a);
                }
            }
        }
        return match.size() == s1.size();
    }

    private boolean isomorphic(String p1, String p2) {
        assertTrue("Solution is wrong length: "+p1, p1.length()==Piece.values().length*3);
        assertTrue("Solution is wrong length: "+p2, p2.length()==Piece.values().length*3);
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();
        for (int i = 0; i < p1.length(); i += 3) {
            s1.add(p1.substring(i, i+3));
            s2.add(p2.substring(i, i+3));
        }
        for(String ss1 : s1) {
            boolean contained = false;
            for (String ss2 : s2) {
                if (ss2.equals(ss1)) contained = true;
            }
            if (!contained) return false;
        }
        return true;
    }
}