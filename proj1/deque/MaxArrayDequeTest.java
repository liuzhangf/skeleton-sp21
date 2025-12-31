package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    //@Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    /*
    public void addIsEmptySizeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        Comparator<Integer> naturalComp = Comparator.naturalOrder();

        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(naturalComp);

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    /*
    public void addRemoveTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        Comparator<Integer> naturalComp = Comparator.naturalOrder();

        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(naturalComp);
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    /*
    public void removeEmptyTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        Comparator<Integer> naturalComp = Comparator.naturalOrder();

        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(naturalComp);
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    /*
    public void multipleParamTest() {

        Comparator<Integer> naturalComp = Comparator.naturalOrder();

        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(naturalComp);
        MaxArrayDeque<Double>  lld2 = new MaxArrayDeque<Double>(naturalComp);
        MaxArrayDeque<Boolean> lld3 = new MaxArrayDeque<Boolean>(naturalComp);

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }*/

    @Test
    public void testMaxWithNullElements() {
        // 空安全比较器：null 视为比任何字符串都小（避免空指针）
        Comparator<String> nullSafeComp = Comparator.nullsFirst(Comparator.naturalOrder());
        MaxArrayDeque<String> deque = new MaxArrayDeque<>(nullSafeComp);

        deque.addLast(null);
        deque.addLast("banana");
        deque.addLast(null);
        deque.addLast("apple");

        assertEquals("包含 null 元素时，max() 应返回非 null 最大值", "banana", deque.max());
    }

    /* Add large number of elements to deque; check if order is correct. */

    @Test
    public void bigLLDequeTest() {
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        Comparator<Integer> naturalComp = Comparator.naturalOrder();
        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(naturalComp);
        for (int i = 0; i < 10000; i++) {
            lld1.addLast(i);
            assertEquals("单元素 max() 应返回该元素", Integer.valueOf(i), lld1.max());
        }
    }
}
