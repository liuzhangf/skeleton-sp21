package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove(){
        BuggyAList<Integer> bug_list = new BuggyAList<>();
        AListNoResizing<Integer> correct  = new AListNoResizing<>();
        assertEquals(correct.size(), bug_list.size());
        bug_list.addLast(4);
        correct.addLast(4);
        bug_list.addLast(5);
        correct.addLast(5);
        bug_list.addLast(6);
        correct.addLast(6);
        assertEquals(bug_list.size(), correct.size());
        assertEquals(correct.removeLast(), bug_list.removeLast());
        assertEquals(correct.removeLast(), bug_list.removeLast());
        assertEquals(correct.removeLast(), bug_list.removeLast());

    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> bug_list = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
                bug_list.addLast(randVal);

            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
                assertEquals(size, bug_list.size());
            }
            else if (operationNumber == 2) {
                if (L.size() > 0) {
                    int elem = L.removeLast();
                    System.out.println("removeLast(" + elem + ")");
                    int bug_elem = bug_list.removeLast();
                    System.out.println("removeLast(" + bug_elem + ")");
                    assertEquals(bug_elem, elem);
                }
            }
            else if (operationNumber == 3) {
                if (L.size() > 0) {
                    int elem = L.getLast();
                    System.out.println("getLast()" + elem);
                    int bug_elem = bug_list.getLast();
                    System.out.println("getLast()" + bug_elem);
                    assertEquals(elem, bug_elem);
                }
            }
        }
    }
}
